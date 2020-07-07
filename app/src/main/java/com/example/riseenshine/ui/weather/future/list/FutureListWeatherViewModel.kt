package com.example.riseenshine.ui.weather.future.list

import androidx.lifecycle.ViewModel
import com.example.riseenshine.data.provider.UnitProvider
import com.example.riseenshine.data.repository.ForecastRepository
import com.example.riseenshine.internal.lazyDeferred
import com.example.riseenshine.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}