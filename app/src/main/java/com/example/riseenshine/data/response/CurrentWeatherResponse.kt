package com.example.riseenshine.data.response


data class CurrentWeatherResponse(
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)