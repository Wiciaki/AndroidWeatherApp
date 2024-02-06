package com.wiktormendalka.weatherapp

import GeocodingApi
import GeocodingResult
import WeatherApi
import WeatherResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel: ViewModel() {
    private val geocodingApi = Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeocodingApi::class.java)

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    val locationData = MutableLiveData<GeocodingResult>()
    var weatherData = MutableLiveData<WeatherResponse>()

    fun fetchLocation(cityName: String) {
        viewModelScope.launch {
            val locationResponse = geocodingApi.getLocationForName(cityName, 1, "en", "json")
            val result = locationResponse.results[0]
            locationData.value = result

            val currentParams = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
            val response = weatherApi.getWeather(result.latitude, result.longitude, currentParams, "auto", 1)
            weatherData.value = response
        }
    }
}