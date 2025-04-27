package com.example.myapplication.ui.ViewModel


import HourlyUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Network.Repository.HourlyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// hourlyviewmodel.kt

//we don't call the Service directly Service(Query url maker) from Model
// We call Repository and that calls Service(Query Builder)
class HourlyViewModel : ViewModel() {
    private val repository = HourlyRepository()

    private val _hourlyState = MutableStateFlow<HourlyUiState>(HourlyUiState.Loading)
    val hourlyState: StateFlow<HourlyUiState> = _hourlyState



    init {
        fetchHourlyForecast()
    }

    fun fetchHourlyForecast(lat: Double = 26.68, long: Double = 90.35) {
        viewModelScope.launch {

            // Process hourly data
            Log.d("HourlyViewModel", "FETCH Hourly METHOD : FROM VIEW MODEL")
            viewModelScope.launch {
                _hourlyState.value = HourlyUiState.Loading
                try {

                    //2. fetchHourly calls getHourly from Repository

                    //FETCH WEATHER CALLS GET WEATHER IN THIS PLACE :: IT INPUTS LAT LONG DEFINED IN HERE
                    repository.getHourlyForecast(lat, long).collect { response ->
                        _hourlyState.value = HourlyUiState.Success(response)
                    }
                } catch (e: Exception) {
                    _hourlyState.value = HourlyUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }


}
