package com.example.riseenshine.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import com.example.riseenshine.data.provider.UnitProvider
import com.example.riseenshine.data.repository.ForecastRepository
import com.example.riseenshine.internal.lazyDeferred
import com.example.riseenshine.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}