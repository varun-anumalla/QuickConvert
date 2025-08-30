package com.varun.quickconvert

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mariuszgromada.math.mxparser.Expression

data class CalculatorUiState(
    val equation: String = "",
    val result: String = "0",
    val isCalculationDone: Boolean = false
)
/**
 * The ViewModel for the [CalculatorScreen].
 *
 * This class is responsible for holding the calculator's state ([CalculatorUiState]) and handling
 * all business logic related to user input and mathematical calculations. It uses the mXparser
 * library to safely evaluate the final mathematical expression.
 */
class CalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * The main entry point for all user actions from the UI.
     * It receives a [CalculatorEvent] and calls the appropriate private function to handle it.
     *
     * @param event The sealed event representing the user's action (e.g., pressing a number or operator).
     */
    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            is CalculatorEvent.Operator -> enterOperator(event.operator)
            CalculatorEvent.Decimal -> enterDecimal()
            CalculatorEvent.Calculate -> calculate()
            CalculatorEvent.Clear -> clear()
            CalculatorEvent.Backspace -> backspace()
            CalculatorEvent.Percentage -> enterPercentage()
        }
    }

    /**
     * Appends a number to the current equation.
     * If a calculation was just completed, it starts a new equation.
     */
    private fun enterNumber(number: String) {
        _uiState.update { currentState ->
            val newEquation = if (currentState.isCalculationDone) {
                number
            } else if (currentState.equation == "0") {
                number
            } else {
                currentState.equation + number
            }
            currentState.copy(equation = newEquation, isCalculationDone = false)
        }
    }

    /**
     * Appends an operator to the current equation.
     * Prevents multiple operators from being entered consecutively.
     */
    private fun enterOperator(operator: String) {
        _uiState.update { currentState ->
            val newEquation = if (currentState.isCalculationDone) {
                currentState.result + operator
            } else {
                if (currentState.equation.isNotBlank() && !currentState.equation.last().isDigit()) {
                    currentState.equation.dropLast(1) + operator
                } else if (currentState.equation.isNotBlank()) {
                    currentState.equation + operator
                } else {
                    currentState.equation
                }
            }
            currentState.copy(equation = newEquation, isCalculationDone = false)
        }
    }


    /**
     * Appends a decimal point, ensuring only one is present per number segment.
     */
    private fun enterDecimal() {
        _uiState.update { currentState ->
            if (currentState.isCalculationDone) return@update currentState

            val lastOperatorIndex = currentState.equation.lastIndexOfAny(charArrayOf('+', '-', '×', '÷'))
            val numberSegment = if (lastOperatorIndex != -1) {
                currentState.equation.substring(lastOperatorIndex + 1)
            } else {
                currentState.equation
            }

            if (!numberSegment.contains(".")) {
                currentState.copy(equation = currentState.equation + ".")
            } else {
                currentState
            }
        }
    }

    private fun calculate() {
        _uiState.update { currentState ->
            if (currentState.equation.isNotBlank()) {
                val expressionString = currentState.equation
                    .replace('×', '*')
                    .replace('÷', '/')

                val expression = Expression(expressionString)
                val result = expression.calculate()

                if (!result.isNaN()) {
                    val formattedResult = if (result % 1 == 0.0) {
                        result.toLong().toString()
                    } else {
                        result.toString()
                    }
                    currentState.copy(
                        result = formattedResult,
                        isCalculationDone = true
                    )
                } else {
                    currentState.copy(result = "Error", isCalculationDone = true)
                }
            } else {
                currentState
            }
        }
    }

    /**
     * Resets the calculator state to its default values.
     */
    private fun clear() {
        _uiState.update {
            CalculatorUiState(equation = "", result = "0", isCalculationDone = false)
        }
    }

    /**
     * Removes the last character from the equation.
     */
    private fun backspace() {
        _uiState.update { currentState ->
            if (currentState.equation.isNotEmpty()) {
                currentState.copy(
                    equation = currentState.equation.dropLast(1),
                    isCalculationDone = false
                )
            } else {
                currentState
            }
        }
    }


    //  function to handle percentage calculations equation like "A + B%", it calculates "A + (A * B / 100)".

    private fun enterPercentage() {
        _uiState.update { currentState ->
            val equation = currentState.equation
            if (equation.isBlank() || currentState.isCalculationDone) return@update currentState

            // Find the last operator (+, -, ×, ÷)
            val lastOperatorIndex = equation.lastIndexOfAny(charArrayOf('+', '-', '×', '÷'))

            if (lastOperatorIndex == -1) return@update currentState

            // Extract the parts of the equation: e.g., "100" and "50" from "100-50"
            val baseNumberString = equation.substring(0, lastOperatorIndex)
            val operator = equation[lastOperatorIndex]
            val percentageNumberString = equation.substring(lastOperatorIndex + 1)

            if (percentageNumberString.isBlank()) return@update currentState

            // Use mxparser to safely evaluate the string parts
            val baseValue = Expression(baseNumberString.replace('×', '*').replace('÷', '/')).calculate()
            val percentageValue = Expression(percentageNumberString).calculate()

            if (baseValue.isNaN() || percentageValue.isNaN()) return@update currentState

            // Calculates the actual value of the percentage
            val resultOfPercentage = (baseValue * percentageValue) / 100

            // Build the new equation string
            val newEquation = "$baseNumberString$operator$resultOfPercentage"

            currentState.copy(equation = newEquation)
        }
    }
}
