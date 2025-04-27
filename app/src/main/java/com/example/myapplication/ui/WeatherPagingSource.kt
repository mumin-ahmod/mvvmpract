package com.example.myapplication.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.data.model.HourlyWeatherItem
import com.example.myapplication.data.model.HourlyWeatherResponse


class WeatherPagingSource(private val data: HourlyWeatherResponse) : PagingSource<Int, HourlyWeatherItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HourlyWeatherItem> {
        return try {
            val page = params.key ?: 0
            val items = data.hourly.time.subList(page * params.loadSize, (page + 1) * params.loadSize)
            LoadResult.Page(
                data = items.map {
                    HourlyWeatherItem(it, data.hourly.temperature_2m[page], data.hourly.weathercode[page])
                },
                prevKey = null,
                nextKey = if (page < data.hourly.time.size / params.loadSize) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HourlyWeatherItem>): Int? {
        TODO("Not yet implemented")
    }
}
