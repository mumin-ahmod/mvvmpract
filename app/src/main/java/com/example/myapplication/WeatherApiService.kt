package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

// WeatherApiService.kt


//actual link gets made in here :  mixed with base url with retrofit
interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current_weather") current: Boolean = true
    ): WeatherResponse

    @GET("v1/forecast")
    suspend fun getHourlyForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("hourly") params: String = "temperature_2m,weathercode"
    ): HourlyWeatherResponse
}