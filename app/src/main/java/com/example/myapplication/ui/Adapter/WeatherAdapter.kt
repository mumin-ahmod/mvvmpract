package com.example.myapplication.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.HourlyWeatherItem
import com.example.myapplication.databinding.ItemWeatherBinding

//weather a
class WeatherAdapter:
    PagingDataAdapter<HourlyWeatherItem, WeatherAdapter.WeatherViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    //recycler view
    inner class WeatherViewHolder(private val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyWeatherItem) {
            binding.timeText.text = item.time
            binding.tempText.text = "${item.temperature}°C"
            binding.codeText.text = "Code: ${item.weatherCode}"
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<HourlyWeatherItem>() {
            override fun areItemsTheSame(oldItem: HourlyWeatherItem, newItem: HourlyWeatherItem): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: HourlyWeatherItem, newItem: HourlyWeatherItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
