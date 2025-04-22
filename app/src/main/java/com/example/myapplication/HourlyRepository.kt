package com.example.myapplication


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// WeatherRepository.kt
class HourlyRepository {


    private val api = RetrofitInstance.weatherApi


    //returns a Flow<WeatherResponse> means it emits weather data - 1 or more times


    //3. Calls Retrofit and Updates flow using emit()
    suspend fun getHourlyForecast(lat: Double, long: Double): Flow<HourlyWeatherResponse> {
        return flow {

            // the URL actually gets made in here :: using get- from api service
            val response = api.getHourlyForecast(lat, long)

            //flow Listens to this

            Log.d("HOURLY RESPONSE:", "$response")

            //emit(response) sends this data downstream to whoever is collecting the flow
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}