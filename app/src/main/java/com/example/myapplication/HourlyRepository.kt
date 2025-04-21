package com.example.myapplication


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// WeatherRepository.kt
class HourlyRepository {


    private val api = RetrofitInstance.weatherApi


    suspend fun getHourlyForecast(lat: Double, long: Double): Flow<HourlyWeatherResponse> {
        return flow {

            // the URL actually gets made in here :: using get- from api service
            val response = api.getHourlyForecast(lat, long)

            //flow Listens to this

            Log.d("HOURLY RESPONSE:", "$response")
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}