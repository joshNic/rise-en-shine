package com.example.riseenshine.data.repository

import androidx.lifecycle.LiveData
import com.example.riseenshine.data.db.entity.WeatherLocation
import com.example.riseenshine.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}