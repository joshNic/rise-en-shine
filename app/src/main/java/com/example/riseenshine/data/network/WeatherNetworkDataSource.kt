package com.example.riseenshine.data.network

import androidx.lifecycle.LiveData
import com.example.riseenshine.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather:LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location:String,
        languageCode:String
    )
}