package com.example.riseenshine.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.riseenshine.data.network.response.CurrentWeatherResponse
import com.example.riseenshine.data.network.response.FutureWeatherResponse
import com.example.riseenshine.internal.Exceptions


const val FORECAST_DAYS_COUNT = 7

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

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    ) {
        try {
            val fetchedFutureWeather = weatherApiService
                .getFutureWeather(location, FORECAST_DAYS_COUNT, languageCode)

            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: Exceptions) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}