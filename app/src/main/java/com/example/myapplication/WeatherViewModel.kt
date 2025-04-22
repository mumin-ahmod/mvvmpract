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

//making a viewmodel by extending viewmodel() class
class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val repository_hourly = HourlyRepository()

    //_weatherState is a mutable flow, holding the current UI state (Loading, Success, or Error).
    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    //weatherState is the read-only version exposed to the UI (Activity or Fragment).
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    fun fetchWeather(lat: Double = 26.68, long: Double = 90.35) {

        Log.d("TAG", "FETCH WEATHER METHOD : FROM VIEW MODEL")
        viewModelScope.launch {

            //Notify the UI that loading has started
            //Your UI can now show a spinner, etc.
            _weatherState.value = WeatherUiState.Loading
            try {

                //You don’t call the API directly in the ViewModel — you call the repo instead.
                //FETCH WEATHER CALLS GET WEATHER IN THIS PLACE :: IT INPUTS LAT LONG DEFINED IN HERE

                //getWeather() returns a Flow, you collect{} it to get the result, you are observing it

                repository.getWeather(lat, long).collect { response ->
                    _weatherState.value = WeatherUiState.Success(response)
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "\"Hourly Forecast: Unknown error\"")
            }
        }
    }


}
