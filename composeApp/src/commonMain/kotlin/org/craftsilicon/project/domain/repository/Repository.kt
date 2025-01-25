package org.craftsilicon.project.domain.repository

import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.craftsilicon.project.data.remote.CraftSiliconClient
import org.craftsilicon.project.data.repository.CraftSiliconApi
import org.craftsilicon.project.db.CraftSilliconDb
import org.craftsilicon.project.domain.model.weather.WeatherResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//Todo--Set cache stale policy

class Repository(
    private val cryptoClient: CraftSiliconClient,
) : CraftSiliconApi, KoinComponent {
    private val database: CraftSilliconDb by inject()
    override suspend fun getWeatherForecast(
        city: String,
        apiKey: String,
        units: String
    ): WeatherResponse {
        val cachedWeather = getWeatherFromCache(city)
        if (cachedWeather != null) {
            // Return cached data if available
            return cachedWeather
        }
        // If no cached data, fetch from the API
        val apiResponse = cryptoClient.getWeatherForecast(city, apiKey, units)
        // Cache the newly fetched data
        cacheWeatherData(city, apiResponse)
        return apiResponse
    }
    private fun getWeatherFromCache(city: String): WeatherResponse? {
        val cachedData = database.weatherEntityQueries.getWeather(city).executeAsOneOrNull()
        return cachedData?.let {
            Json.decodeFromString<WeatherResponse>(it.weatherData)
        }
    }
    private fun cacheWeatherData(city: String, weatherResponse: WeatherResponse) {
        val weatherDataString = Json.encodeToString(weatherResponse)
        database.weatherEntityQueries.insertWeather(
            cityName = city,
            weatherData = weatherDataString,
            lastUpdated = Clock.System.now().toEpochMilliseconds()
        )
    }
}