package com.varun.quickconvert

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

enum class TemperatureUnit(val displayName: String, val symbol: String) {
    CELSIUS("Degree Celsius", "°C"),
    FAHRENHEIT("Degree Fahrenheit", "°F"),
    KELVIN("Kelvin", "K")
}

data class TemperatureUiState(
    val fromValue: String = "",
    val toValue: String = "",
    val fromUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val toUnit: TemperatureUnit = TemperatureUnit.FAHRENHEIT,
    val isFromFieldActive: Boolean = true // Tracks which field is being edited
)

sealed interface TemperatureEvent {
    data class NumberPressed(val number: String) : TemperatureEvent
    object DecimalPressed : TemperatureEvent
    object ClearPressed : TemperatureEvent
    object BackspacePressed : TemperatureEvent
    object ToggleSignPressed : TemperatureEvent
    object SetFromFieldActive : TemperatureEvent
    object SetToFieldActive : TemperatureEvent
    data class ChangeFromUnit(val unit: TemperatureUnit) : TemperatureEvent
    data class ChangeToUnit(val unit: TemperatureUnit) : TemperatureEvent
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState = _uiState.asStateFlow()

    private val decimalFormat = DecimalFormat("#.##")

    fun onEvent(event: TemperatureEvent) {
        when (event) {
            is TemperatureEvent.NumberPressed -> appendNumber(event.number)
            is TemperatureEvent.DecimalPressed -> appendDecimal()
            is TemperatureEvent.ClearPressed -> clearActiveField()
            is TemperatureEvent.BackspacePressed -> performBackspace()
            is TemperatureEvent.ToggleSignPressed -> toggleSign()
            is TemperatureEvent.SetFromFieldActive -> _uiState.update { it.copy(isFromFieldActive = true) }
            is TemperatureEvent.SetToFieldActive -> _uiState.update { it.copy(isFromFieldActive = false) }
            is TemperatureEvent.ChangeFromUnit -> {
                _uiState.update { it.copy(fromUnit = event.unit) }
                recalculate()
            }
            is TemperatureEvent.ChangeToUnit -> {
                _uiState.update { it.copy(toUnit = event.unit) }
                recalculate()
            }
        }
    }

    private fun appendNumber(number: String) {
        val activeValue = getActiveValue()
        val newValue = if (activeValue == "0") number else activeValue + number
        updateStateWithValue(newValue)
    }

    private fun appendDecimal() {
        val activeValue = getActiveValue()
        if (!activeValue.contains(".")) {
            val newValue = if (activeValue.isEmpty()) "0." else "$activeValue."
            updateStateWithValue(newValue)
        }
    }

    private fun clearActiveField() {
        updateStateWithValue("")
    }

    private fun performBackspace() {
        val activeValue = getActiveValue()
        updateStateWithValue(activeValue.dropLast(1))
    }

    private fun toggleSign() {
        val activeValue = getActiveValue()
        if (activeValue.isNotBlank()) {
            val newValue = if (activeValue.startsWith("-")) {
                activeValue.removePrefix("-")
            } else {
                "-$activeValue"
            }
            updateStateWithValue(newValue)
        }
    }

    private fun getActiveValue(): String {
        val state = _uiState.value
        return if (state.isFromFieldActive) state.fromValue else state.toValue
    }

    private fun updateStateWithValue(newValue: String) {
        val state = _uiState.value
        if (state.isFromFieldActive) {
            _uiState.update { it.copy(fromValue = newValue) }
        } else {
            _uiState.update { it.copy(toValue = newValue) }
        }
        recalculate()
    }

    private fun recalculate() {
        val state = _uiState.value
        val (valueToConvert, sourceUnit, targetUnit) = if (state.isFromFieldActive) {
            Triple(state.fromValue, state.fromUnit, state.toUnit)
        } else {
            Triple(state.toValue, state.toUnit, state.fromUnit)
        }

        val number = valueToConvert.toDoubleOrNull()

        if (number == null) {
            if (state.isFromFieldActive) _uiState.update { it.copy(toValue = "") }
            else _uiState.update { it.copy(fromValue = "") }
            return
        }

        // for now i only convert C <-> F
        val convertedValue = when {
            sourceUnit == TemperatureUnit.CELSIUS && targetUnit == TemperatureUnit.FAHRENHEIT -> (number * 9 / 5) + 32
            sourceUnit == TemperatureUnit.FAHRENHEIT && targetUnit == TemperatureUnit.CELSIUS -> (number - 32) * 5 / 9
            else -> number // Placeholder for other conversions
        }

        if (state.isFromFieldActive) {
            _uiState.update { it.copy(toValue = decimalFormat.format(convertedValue)) }
        } else {
            _uiState.update { it.copy(fromValue = decimalFormat.format(convertedValue)) }
        }
    }
}
