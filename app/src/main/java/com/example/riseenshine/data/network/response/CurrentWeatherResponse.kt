package com.example.riseenshine.data.network.response

import com.example.riseenshine.data.db.entity.CurrentWeatherEntry
import com.example.riseenshine.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(

    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)