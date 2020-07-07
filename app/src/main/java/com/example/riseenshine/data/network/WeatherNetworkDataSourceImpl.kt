package com.example.riseenshine.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.riseenshine.data.network.response.CurrentWeatherResponse
import com.example.riseenshine.internal.Exceptions

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchCurrentWeather = weatherApiService
                .getCurrentWeather(location,languageCode)
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        }
        catch (e:Exceptions){
            Log.e("Connectivity","No internet connection on this",e)
        }
    }
}