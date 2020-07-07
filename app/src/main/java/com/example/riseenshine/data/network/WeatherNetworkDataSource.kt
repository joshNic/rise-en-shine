package com.example.riseenshine.data.network

import androidx.lifecycle.LiveData
import com.example.riseenshine.data.network.response.CurrentWeatherResponse
import com.example.riseenshine.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather:LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location:String,
        languageCode:String
    )
    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )
}