package com.example.myapplication

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// WeatherRepository.kt
class WeatherRepository {


    private val api = RetrofitInstance.weatherApi

    suspend fun getWeather(lat: Double, long: Double): Flow<WeatherResponse> {
        return flow {

            // the URL actually gets made in here :: using get-weather
            val response = api.getWeather(lat, long)

           Log.d("RESPONSE", "$response")
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getHourlyForecast(lat: Double, long: Double): Flow<WeatherResponse> {
        return flow {

            // the URL actually gets made in here :: using get- from api service
            val response = api.getHourlyForecast(lat, long)

            //flow Listens to this

            Log.d("RESPONSE", "$response")
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}