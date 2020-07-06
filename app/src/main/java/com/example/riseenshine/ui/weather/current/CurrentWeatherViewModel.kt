package com.example.riseenshine.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.riseenshine.data.repository.ForecastRepository
import com.example.riseenshine.internal.UnitSystem
import com.example.riseenshine.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}