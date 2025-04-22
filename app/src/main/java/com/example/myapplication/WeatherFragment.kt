package com.example.myapplication

import HourlyUiState
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentWeatherBinding
import kotlinx.coroutines.launch
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val hourlyViewModel: HourlyViewModel by activityViewModels() // shared with MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRefreshHourly.setOnClickListener {
            hourlyViewModel.fetchHourlyForecast()
        }

        binding.weatherRecycler.layoutManager = LinearLayoutManager(requireContext())

        collectWeatherState()
    }

    private fun collectWeatherState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                hourlyViewModel.hourlyState.collect { state ->
                    when (state) {
                        is HourlyUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.weatherRecycler.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                        }
                        is HourlyUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            showWeather(state.weather)
                        }
                        is HourlyUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.weatherRecycler.visibility = View.GONE
                            binding.errorText.visibility = View.VISIBLE
                            binding.errorText.text = state.message
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showWeather(data: HourlyWeatherResponse) {
        val items = data.hourly.time.indices.map { i ->
            HourlyWeatherItem(
                time = data.hourly.time[i],
                temperature = data.hourly.temperature_2m[i],
                weatherCode = data.hourly.weathercode[i]
            )
        }
        val adapter = WeatherAdapter(items)
        binding.weatherRecycler.adapter = adapter
        binding.weatherRecycler.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
