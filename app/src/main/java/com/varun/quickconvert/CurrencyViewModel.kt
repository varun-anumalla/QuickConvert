package com.varun.quickconvert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// --- 1. UI State ---
data class CurrencyUiState(
    val fromValue: String = "1",
    val toValue: String = "",
    val fromCurrency: Currency = worldCurrencies.find { it.code == "USD" } ?: worldCurrencies.first(),
    val toCurrency: Currency = worldCurrencies.find { it.code == "INR" } ?: worldCurrencies.first(),
    val isFromFieldActive: Boolean = true,
    val rates: Map<String, Double> = emptyMap(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val availableCurrencies: List<Currency> = worldCurrencies
)

// --- 2. User Events ---
// Defines all actions the user can perform.
sealed interface CurrencyEvent {
    data class NumberPressed(val number: String) : CurrencyEvent
    object DecimalPressed : CurrencyEvent
    object ClearPressed : CurrencyEvent
    object BackspacePressed : CurrencyEvent
    object SetFromFieldActive : CurrencyEvent
    object SetToFieldActive : CurrencyEvent
    data class ChangeFromCurrency(val currency: Currency) : CurrencyEvent
    data class ChangeToCurrency(val currency: Currency) : CurrencyEvent
}

// --- 3. The ViewModel ---
class CurrencyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState = _uiState.asStateFlow()

    private val decimalFormat = DecimalFormat("#.##")

// API key
    private val apiKey = "a3e424e47716944f2ccc69a5"

    init {
        // Fetch rates as soon as the ViewModel is created
        fetchRates()
    }

    // Main entry point for all UI events
    fun onEvent(event: CurrencyEvent) {
        when (event) {
            is CurrencyEvent.NumberPressed -> appendNumber(event.number)
            is CurrencyEvent.DecimalPressed -> appendDecimal()
            is CurrencyEvent.ClearPressed -> clearActiveField()
            is CurrencyEvent.BackspacePressed -> performBackspace()
            is CurrencyEvent.SetFromFieldActive -> _uiState.update { it.copy(isFromFieldActive = true) }
            is CurrencyEvent.SetToFieldActive -> _uiState.update { it.copy(isFromFieldActive = false) }
            is CurrencyEvent.ChangeFromCurrency -> {
                _uiState.update { it.copy(fromCurrency = event.currency) }
                fetchRates() // Fetch new rates for the new base currency
            }
            is CurrencyEvent.ChangeToCurrency -> {
                _uiState.update { it.copy(toCurrency = event.currency) }
                recalculate() // No need to fetch, just recalculate with existing rates
            }
        }
    }

    private fun fetchRates() {
        val baseCurrency = _uiState.value.fromCurrency.code
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getLatestRates(apiKey, baseCurrency)
                if (response.result == "success") {
                    _uiState.update { it.copy(rates = response.rates, isLoading = false) }
                    recalculate() // Recalculate with the new rates
                } else {
                    _uiState.update { it.copy(error = "API Error", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Network Error", isLoading = false) }
            }
        }
    }

    private fun recalculate() {
        val state = _uiState.value
        val fromValueDouble = state.fromValue.toDoubleOrNull()
        if (fromValueDouble == null || state.rates.isEmpty()) {
            _uiState.update { it.copy(toValue = "") }
            return
        }

        val rate = state.rates[state.toCurrency.code] ?: 0.0
        val result = fromValueDouble * rate
        _uiState.update { it.copy(toValue = decimalFormat.format(result)) }
    }

    private fun appendNumber(number: String) {
        val activeValue = if (_uiState.value.isFromFieldActive) _uiState.value.fromValue else ""
        if (activeValue.length >= 12) return // Limit input length

        val newValue = if (activeValue == "0") number else activeValue + number
        updateStateWithValue(newValue)
    }

    private fun appendDecimal() {
        val activeValue = if (_uiState.value.isFromFieldActive) _uiState.value.fromValue else ""
        if (!activeValue.contains(".")) {
            val newValue = if (activeValue.isEmpty()) "0." else "$activeValue."
            updateStateWithValue(newValue)
        }
    }

    private fun clearActiveField() {
        updateStateWithValue("")
    }

    private fun performBackspace() {
        val activeValue = if (_uiState.value.isFromFieldActive) _uiState.value.fromValue else ""
        updateStateWithValue(activeValue.dropLast(1))
    }

    private fun updateStateWithValue(newValue: String) {
        if (_uiState.value.isFromFieldActive) {
            _uiState.update { it.copy(fromValue = newValue) }
        }
        recalculate()
    }
}