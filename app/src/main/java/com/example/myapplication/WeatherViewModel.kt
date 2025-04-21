package com.example.myapplication

import HourlyUiState
import WeatherUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// WeatherViewModel.kt

//we don't call the Service directly Service(Query url maker) from Model
// We call Repository and that calls Service(Query Builder)
class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val repository_hourly = HourlyRepository()

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    fun fetchWeather(lat: Double = 26.68, long: Double = 90.35) {

        Log.d("TAG", "FETCH WEATHER METHOD : FROM VIEW MODEL")
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {

                //FETCH WEATHER CALLS GET WEATHER IN THIS PLACE :: IT INPUTS LAT LONG DEFINED IN HERE
                repository.getWeather(lat, long).collect { response ->
                    _weatherState.value = WeatherUiState.Success(response)
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "\"Hourly Forecast: Unknown error\"")
            }
        }
    }

    fun fetchHourlyForecast(lat: Double = 52.52, long: Double = 13.41) {
        viewModelScope.launch {

            Log.d("TAG", "FETCH Hourly METHOD : FROM VIEW MODEL")
            // Process hourly data

            viewModelScope.launch {
                _weatherState.value = WeatherUiState.Loading
                try {

                    //FETCH WEATHER CALLS GET WEATHER IN THIS PLACE :: IT INPUTS LAT LONG DEFINED IN HERE
                    repository_hourly.getHourlyForecast(lat, long).collect { response ->
                        _weatherState.value = HourlyUiState.Success(response)
                    }
                } catch (e: Exception) {
                    _weatherState.value = HourlyUiState.Error(e.message ?: "Hourly Forecast: Unknown error")
                }
            }
        }
    }
}
