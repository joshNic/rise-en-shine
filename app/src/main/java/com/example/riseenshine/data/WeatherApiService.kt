package com.example.riseenshine.data

import com.example.riseenshine.data.response.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val  API_KEY = "053c944154dc4110b0f23838200804"

//http://api.weatherstack.com/current?access_key=a6e7469e809c05563fe9feda9d4b38c4&query=london
// https://api.weatherapi.com/v1/current.json?key=053c944154dc4110b0f23838200804&q=kampala&lang=en

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): CurrentWeatherResponse



    companion object {
        operator fun invoke(): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}