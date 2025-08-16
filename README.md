# QuickConvert: A Modern Android Multi-Tool App

A sleek and modern multi-tool application for Android, built entirely with **Jetpack Compose** and **Material 3**. This app serves as a comprehensive portfolio piece showcasing modern UI/UX principles, a clean **MVVM architecture**, and integration with a live web service for real-time data.

## ✨ App Screenshots & Demo

*(To add your screenshots: Take screenshots of your app, drag them into the GitHub editor here, and replace these placeholder links.)*

| Home Screen | Calculator | Temperature Converter | Currency Converter |
| :---: | :---: | :---: | :---: |
| <img src="URL_TO_YOUR_HOME_SCREENSHOT.png" width="200"> | <img src="URL_TO_YOUR_CALCULATOR_SCREENSHOT.png" width="200"> | <img src="URL_TO_YOUR_TEMPERATURE_SCREENSHOT.png" width="200"> | <img src="URL_TO_YOUR_CURRENCY_SCREENSHOT.png" width="200"> |

**App Demo (GIF):**

![App Demo GIF](URL_TO_YOUR_APP_DEMO.gif)

## 🌟 Features

* **💰 Live Currency Converter:** Fetches real-time exchange rates from a REST API to provide accurate conversions between over 150 world currencies.
* **🌡️ Temperature & 🏎️ Speed Converters:** Two highly interactive converters for various units (Celsius, Fahrenheit, Kelvin, km/h, mph, etc.). Both feature a custom keypad and large, impressive display boxes that highlight the active field.
* **🧮 Modern Calculator:** A beautiful and functional calculator built with a strong focus on UI/UX. It features a professional layout, satisfying button-press animations, and robust logic for all standard operations.
* **🎨 Polished & Consistent UI:**
    * Built entirely with **Jetpack Compose** and **Material 3**.
    * The Home, Temperature, Speed, and Currency screens are locked into a **static light theme** for a consistent and clean brand identity, regardless of the system's theme.
    * The Calculator screen correctly adapts to the system's **Light and Dark modes**.
* **👆 Realistic Click Animations:** The home screen cards feature a satisfying press-and-release animation. Navigation is intelligently delayed to allow the animation to complete, creating a smoother and more polished user experience.

## 🛠️ Tech Stack & Architecture

This project was built using a modern Android tech stack and follows the recommended **MVVM (Model-View-ViewModel)** architecture.

### Tech Stack
* **Kotlin**: The official programming language for Android development.
* **Jetpack Compose**: Android's modern, declarative UI toolkit for building native UI.
* **Material 3**: The latest version of Google's design system.
* **Coroutines**: For managing background threads and asynchronous operations, especially for API calls and animation delays.
* **Retrofit & Gson**: The industry-standard libraries for making network requests to the REST API and parsing JSON data.
* **Jetpack Navigation**: For handling all in-app navigation between screens in a lifecycle-aware manner.
* **ViewModel**: To store and manage UI-related data, ensuring data survives configuration changes.

### Architecture
* **MVVM (Model-View-ViewModel)**: The app is structured using the MVVM pattern, which cleanly separates the UI (View) from the business logic and state (ViewModel). Each of the four tools has its own dedicated ViewModel to manage its state and handle user events, promoting a clean and scalable codebase.

## 🚀 How to Build

To build and run this project, you will need to:

1.  Clone this repository.
2.  Open the project in a recent version of Android Studio (Hedgehog or newer is recommended).
3.  Obtain a free API key from [exchangerate-api.com](https://www.exchangerate-api.com).
4.  Open the file `app/src/main/java/com/varun/quickconvert/CurrencyViewModel.kt`.
5.  Replace the placeholder API key with your own:
    ```kotlin
    private val apiKey = "YOUR_API_KEY_HERE"
    ```
6.  Build and run the app.

## 🙏 Acknowledgments

Currency conversion data is provided by the [ExchangeRate-API](https://www.exchangerate-api.com).
