package com.varun.quickconvert

/**
 * A sealed interface represents a restricted hierarchy.
 * This is perfect for defining all possible events (user actions) on the calculator screen.
 * Using a sealed interface ensures that our `when` statement in the ViewModel covers all cases.
 */
sealed interface CalculatorEvent {
    // Represents pressing a number button
    data class Number(val number: String) : CalculatorEvent

    // Represents pressing an operator button (+, -, ×, ÷)
    data class Operator(val operator: String) : CalculatorEvent

    // Represents pressing the "AC" (All Clear) button
    object Clear : CalculatorEvent

    // Represents pressing the "=" (Equals) button
    object Calculate : CalculatorEvent

    // Represents pressing the "." (Decimal) button
    object Decimal : CalculatorEvent

    // Represents pressing the "⌫" (Backspace) button
    object Backspace : CalculatorEvent

     // represents pressing the "%" (Percentage) button
    object Percentage : CalculatorEvent

}
