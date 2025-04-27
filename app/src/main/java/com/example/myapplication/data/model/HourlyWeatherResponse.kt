package com.example.myapplication.data.model

data class HourlyWeatherResponse(
    val hourly: HourlyData
)

data class HourlyData (
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weathercode: List<Int>
)

data class HourlyWeatherItem(
    val time: String,
    val temperature: Double,
    val weatherCode: Int
)