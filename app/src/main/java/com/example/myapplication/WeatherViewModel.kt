package com.example.myapplication

import WeatherUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// WeatherViewModel.kt
class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    fun fetchWeather(lat: Double = 52.52, long: Double = 13.41) {

        Log.d("TAG", "FETCH WEATHER METHOD : FROM VIEW MODEL")
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {
                repository.getWeather(lat, long).collect { response ->
                    _weatherState.value = WeatherUiState.Success(response)
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
