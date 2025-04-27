package com.example.myapplication.Network.Repository

import android.util.Log
import com.example.myapplication.Network.API.RetrofitInstance
import com.example.myapplication.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// WeatherRepository.kt
class WeatherRepository {

    private val api = RetrofitInstance.weatherApi

    //returns a Flow<WeatherResponse> means it emits weather data - 1 or more times
    suspend fun getWeather(lat: Double, long: Double): Flow<WeatherResponse> {
        return flow {

            // the URL actually gets made in here :: using get-weather
            val response = api.getWeather(lat, long)

           Log.d("RESPONSE", "$response")
            emit(response)
        }.flowOn(Dispatchers.IO)
    }


}