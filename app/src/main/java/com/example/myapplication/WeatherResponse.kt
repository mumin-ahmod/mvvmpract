package com.example.myapplication

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

data class HourlyWeatherResponse(
   val hourly: HourlyData
)

data class HourlyData (
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weathercode: List<Int>
)
