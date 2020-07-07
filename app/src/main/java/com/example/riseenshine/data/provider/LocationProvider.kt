package com.example.riseenshine.data.provider

import com.example.riseenshine.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    suspend fun getPreferredLocationString():String
}