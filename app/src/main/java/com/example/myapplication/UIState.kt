import com.example.myapplication.HourlyWeatherResponse
import com.example.myapplication.WeatherResponse

// UiState.kt
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

sealed class HourlyUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: HourlyWeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}