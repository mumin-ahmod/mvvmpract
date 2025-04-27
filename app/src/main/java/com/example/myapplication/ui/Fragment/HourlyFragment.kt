package com.example.myapplication.ui.Fragment

import HourlyUiState
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ui.Adapter.WeatherAdapter
import com.example.myapplication.ui.WeatherPagingSource
import com.example.myapplication.data.model.HourlyWeatherResponse
import com.example.myapplication.databinding.FragmentHourlyBinding
import com.example.myapplication.ui.ViewModel.HourlyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HourlyFragment : Fragment() {

    private var _binding: FragmentHourlyBinding? = null
    private val binding get() = _binding!!
    private val hourlyViewModel: HourlyViewModel by activityViewModels()

    private val weatherAdapter = WeatherAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHourlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherRecycler.adapter = weatherAdapter

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
                        }
                        is HourlyUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.weatherRecycler.visibility = View.VISIBLE
                            showWeather(state.weather)
                        }
                        is HourlyUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.weatherRecycler.visibility = View.GONE
                            binding.errorText.visibility = View.VISIBLE
                            binding.errorText.text = state.message
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun showWeather(data: HourlyWeatherResponse) {
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { WeatherPagingSource(data) }
        ).flow.cachedIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            pager.collectLatest {
                weatherAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
