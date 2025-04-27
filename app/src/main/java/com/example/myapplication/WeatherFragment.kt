package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentWeatherBinding
import kotlinx.coroutines.launch

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()

    //declaring nav controller variable
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupObservers()

        viewModel.fetchWeather() //calling fetch weather

        binding.btnRefresh.setOnClickListener{
            viewModel.fetchWeather()
        }



        binding.btnHourly.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment2_to_hourlyFragment2)
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
            binding.timeText.text = "Updated: ${time.substring(11)}" // Just show time
        }
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.weatherContainer.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

}