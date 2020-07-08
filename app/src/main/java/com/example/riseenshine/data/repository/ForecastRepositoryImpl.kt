package com.example.riseenshine.data.repository

import androidx.lifecycle.LiveData
import com.example.riseenshine.data.db.CurrentWeatherDao
import com.example.riseenshine.data.db.FutureWeatherDao
import com.example.riseenshine.data.db.WeatherLocationDao
import com.example.riseenshine.data.db.entity.WeatherLocation
import com.example.riseenshine.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.example.riseenshine.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.example.riseenshine.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.example.riseenshine.data.network.FORECAST_DAYS_COUNT
import com.example.riseenshine.data.network.WeatherNetworkDataSource
import com.example.riseenshine.data.network.response.CurrentWeatherResponse
import com.example.riseenshine.data.network.response.FutureWeatherResponse
import com.example.riseenshine.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource:WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        weatherNetworkDataSource.apply {
            downloadCurrentWeather.observeForever { newCurrentWeather->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }

    }

    private fun persistFetchedCurrentWeather(newCurrentWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(newCurrentWeather.currentWeatherEntry)
            weatherLocationDao.upsert(newCurrentWeather.location)
        }

    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitSpecificDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getDetailedWeatherByDateMetric(date)
            else futureWeatherDao.getDetailedWeatherByDateImperial(date)
        }
    }
    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {

        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }
    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if (isFetchFutureNeeded())
            fetchFutureWeather()
    }
    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }
    private fun isFetchCurrentNeeded(lastFetchedTime:ZonedDateTime):Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }
}