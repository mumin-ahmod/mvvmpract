package com.example.myapplication.data.model

// WeatherResponse.kt
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current_weather: CurrentWeather
)

data class CurrentWeather(
    val temperature: Float,
    val windspeed: Float,
    val winddirection: Int,
    val weathercode: Int,
    val time: String
)

