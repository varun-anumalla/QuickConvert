package com.varun.quickconvert

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- 1. Data Model for the API Response ---
// This class represents the JSON structure we expect from the API.
data class ApiResponse(
    val result: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val rates: Map<String, Double>
)

// --- 2. Retrofit API Service Interface ---
// Defines the endpoint we will call.
interface CurrencyApiService {
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String
    ): ApiResponse
}

// --- 3. Retrofit Client Singleton ---
// This object creates and provides a single instance of our API service.
object RetrofitClient {
    private const val BASE_URL = "https://v6.exchangerate-api.com/"

    // Create a logger to see request and response details in Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}