package com.example.riseenshine.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.riseenshine.data.provider.UnitProvider
import com.example.riseenshine.data.repository.ForecastRepository
import com.example.riseenshine.internal.UnitSystem
import com.example.riseenshine.internal.lazyDeferred
import com.example.riseenshine.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }
}