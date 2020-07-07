package com.example.riseenshine.ui.base

import androidx.lifecycle.ViewModel
import com.example.riseenshine.data.provider.UnitProvider
import com.example.riseenshine.data.repository.ForecastRepository
import com.example.riseenshine.internal.UnitSystem
import com.example.riseenshine.internal.lazyDeferred

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}