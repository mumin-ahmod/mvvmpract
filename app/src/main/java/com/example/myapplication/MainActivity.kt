package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()


    private val hourlyViewModel: HourlyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Directly show the WeatherFragment in the fragment container
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WeatherFragment())
                .commit()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            // Explicitly specify the lifecycle state

            Log.d("TAG", "SETUP OBSERVER")

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherState.collect { state ->
                    when (state) {

                        //we made 3 fun to show different progress
                        //based on state
                        is WeatherUiState.Loading -> showLoading()
                        is WeatherUiState.Success -> showWeather(state.weather)
                        is WeatherUiState.Error -> showError(state.message)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.weatherContainer.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    private fun showWeather(weather: WeatherResponse) {
        binding.progressBar.visibility = View.GONE
        binding.weatherContainer.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        with(weather.current_weather) {
            binding.temperatureText.text = "${temperature}°C"
            binding.windText.text = "Wind: ${windspeed} km/h"
            binding.WtimeText.text = "Updated: ${time.substring(11)}" // Just show time
        }
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.weatherContainer.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }
}