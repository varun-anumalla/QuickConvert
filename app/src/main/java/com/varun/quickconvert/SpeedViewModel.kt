package com.varun.quickconvert

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

// Enum to define all the speed units that will be supporting.
// This makes the code much cleaner than using simple strings.
enum class SpeedUnit(val displayName: String, val symbol: String) {
    KILOMETERS_PER_HOUR("Kilometers/hour", "km/h"),
    MILES_PER_HOUR("Miles/hour", "mph"),
    METERS_PER_SECOND("Meters/second", "m/s"),
    KILOMETERS_PER_SECOND("Kilometers/second", "km/s")
}

// Data class to hold the entire state of the UI in one object.
// This makes it easy to manage and update the screen.
data class SpeedUiState(
    val fromValue: String = "",
    val toValue: String = "",
    val fromUnit: SpeedUnit = SpeedUnit.KILOMETERS_PER_HOUR,
    val toUnit: SpeedUnit = SpeedUnit.MILES_PER_HOUR,
    val isFromFieldActive: Boolean = true // To know which field is currently being edited
)

// A sealed interface to define all possible user actions (events) on the screen.
// Using a sealed interface is great because it forces me to handle every possible event.
sealed interface SpeedEvent {
    data class NumberPressed(val number: String, val context: Context) : SpeedEvent
    object DecimalPressed : SpeedEvent
    object ClearPressed : SpeedEvent
    object BackspacePressed : SpeedEvent
    object SetFromFieldActive : SpeedEvent
    object SetToFieldActive : SpeedEvent
    data class ChangeFromUnit(val unit: SpeedUnit) : SpeedEvent
    data class ChangeToUnit(val unit: SpeedUnit) : SpeedEvent
}

// The ViewModel is the brain of the screen. It holds the state and all the business logic.
class SpeedViewModel : ViewModel() {
    // _uiState is the private, mutable version of the state. Only the ViewModel can change it.
    private val _uiState = MutableStateFlow(SpeedUiState())
    // uiState is the public, read-only version that the UI can observe for changes.
    val uiState = _uiState.asStateFlow()

    // Used to format the final result to 4 decimal places for a clean look.
    private val decimalFormat = DecimalFormat("#.####")

    // This is the main entry point for all events from the UI.
    fun onEvent(event: SpeedEvent) {
        when (event) {
            is SpeedEvent.NumberPressed -> appendNumber(event.number, event.context)
            is SpeedEvent.DecimalPressed -> appendDecimal()
            is SpeedEvent.ClearPressed -> clearActiveField()
            is SpeedEvent.BackspacePressed -> performBackspace()
            is SpeedEvent.SetFromFieldActive -> _uiState.update { it.copy(isFromFieldActive = true) }
            is SpeedEvent.SetToFieldActive -> _uiState.update { it.copy(isFromFieldActive = false) }
            is SpeedEvent.ChangeFromUnit -> {
                _uiState.update { it.copy(fromUnit = event.unit) }
                recalculate()
            }
            is SpeedEvent.ChangeToUnit -> {
                _uiState.update { it.copy(toUnit = event.unit) }
                recalculate()
            }
        }
    }

    // Handles adding a new number to the active input field.
    private fun appendNumber(number: String, context: Context) {
        val activeValue = getActiveValue()
        // I need to check the length of the number without the decimal to enforce the limit.
        val cleanValue = activeValue.replace(".", "").removePrefix("-")
        if (cleanValue.length >= 12) {
            Toast.makeText(context, "Maximum digits reached (12)", Toast.LENGTH_SHORT).show()
            return // Stop the function if the limit is reached.
        }

        // If the current value is "0", replace it. Otherwise, append the new number.
        val newValue = if (activeValue == "0") number else activeValue + number
        updateStateWithValue(newValue)
    }

    // Handles adding a decimal point, ensuring there's only one.
    private fun appendDecimal() {
        val activeValue = getActiveValue()
        if (!activeValue.contains(".")) {
            val newValue = if (activeValue.isEmpty()) "0." else "$activeValue."
            updateStateWithValue(newValue)
        }
    }

    // Clears the currently active input field.
    private fun clearActiveField() {
        updateStateWithValue("")
    }

    // this Removes the last character from the active input field.
    private fun performBackspace() {
        val activeValue = getActiveValue()
        updateStateWithValue(activeValue.dropLast(1))
    }

    // A helper function to easily get the string value of the currently active field.
    private fun getActiveValue(): String {
        val state = _uiState.value
        return if (state.isFromFieldActive) state.fromValue else state.toValue
    }

    // Updates the correct state field (from or to) with the new value and triggers a recalculation.
    private fun updateStateWithValue(newValue: String) {
        val state = _uiState.value
        if (state.isFromFieldActive) {
            _uiState.update { it.copy(fromValue = newValue) }
        } else {
            _uiState.update { it.copy(toValue = newValue) }
        }
        recalculate()
    }

    /**
     * This is the main calculation engine. My strategy here is to have a single, standard
     * base unit (Meters per Second) that everything gets converted to and from. This avoids
     * having to write a separate formula for every single combination (like km/h to mph,
     * mph to km/s, etc.). It's much cleaner and easier to maintain.
     *
     * The process is:
     * 1. Take the input value (e.g., 100 km/h).
     * 2. Convert it to the base unit (100 km/h -> 27.77 m/s).
     * 3. Convert the base unit value to the target unit (27.77 m/s -> 62.13 mph).
     */
    private fun recalculate() {
        val state = _uiState.value
        // Figure out which field is the source and which is the target.
        val (valueToConvert, sourceUnit, targetUnit) = if (state.isFromFieldActive) {
            Triple(state.fromValue, state.fromUnit, state.toUnit)
        } else {
            Triple(state.toValue, state.toUnit, state.fromUnit)
        }

        val number = valueToConvert.toDoubleOrNull()

        // If the input is empty or invalid, just clear the other field.
        if (number == null || valueToConvert == "." || valueToConvert == "-") {
            if (state.isFromFieldActive) _uiState.update { it.copy(toValue = "") }
            else _uiState.update { it.copy(fromValue = "") }
            return
        }

        // Step 1: Convert the input value to our base unit (Meters per Second).
        val valueInMetersPerSecond = toMetersPerSecond(number, sourceUnit)

        // Step 2: Convert from the base unit to the final target unit.
        val convertedValue = fromMetersPerSecond(valueInMetersPerSecond, targetUnit)

        // Step 3: Update the correct (inactive) field with the final result.
        if (state.isFromFieldActive) {
            _uiState.update { it.copy(toValue = decimalFormat.format(convertedValue)) }
        } else {
            _uiState.update { it.copy(fromValue = decimalFormat.format(convertedValue)) }
        }
    }

    // This function handles converting any of our units INTO Meters per Second.
    private fun toMetersPerSecond(value: Double, fromUnit: SpeedUnit): Double {
        return when (fromUnit) {
            // Formula: 1 km/h is 1000 meters in 3600 seconds, so value / 3.6
            SpeedUnit.KILOMETERS_PER_HOUR -> value / 3.6
            // Formula: 1 mph is approx 0.44704 m/s. Using 1/2.23694 for better precision.
            SpeedUnit.MILES_PER_HOUR -> value / 2.23694
            // No conversion needed.
            SpeedUnit.METERS_PER_SECOND -> value
            // Formula: 1 km/s is 1000 m/s.
            SpeedUnit.KILOMETERS_PER_SECOND -> value * 1000
        }
    }

    // This function handles converting FROM Meters per Second into any of our other units.
    private fun fromMetersPerSecond(value: Double, toUnit: SpeedUnit): Double {
        return when (toUnit) {
            // Formula: The reverse of the above, so value * 3.6
            SpeedUnit.KILOMETERS_PER_HOUR -> value * 3.6
            // Formula: The reverse of the above, so value * 2.23694
            SpeedUnit.MILES_PER_HOUR -> value * 2.23694
            // No conversion needed.
            SpeedUnit.METERS_PER_SECOND -> value
            // Formula: The reverse of the above, so value / 1000
            SpeedUnit.KILOMETERS_PER_SECOND -> value / 1000
        }
    }
}
