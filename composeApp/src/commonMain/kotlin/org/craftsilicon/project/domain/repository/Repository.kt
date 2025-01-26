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

class Repository(
    private val cryptoClient: CraftSiliconClient,
) : CraftSiliconApi, KoinComponent {
    private val database: CraftSilliconDb by inject()
    override suspend fun getWeatherForecast(
        city: String,
        apiKey: String,
        units: String
    ): Pair<WeatherResponse, Long> {

        val apiResponse = try {
            cryptoClient.getWeatherForecast(city, apiKey, units)
        } catch (e: Exception) {
            val cachedWeather = getWeatherFromCache(city)
            if (cachedWeather != null) {
                return cachedWeather
            } else {
                throw e
            }
        }
        cacheWeatherData(city, apiResponse)
        return apiResponse to Clock.System.now().toEpochMilliseconds()

    }
    /**
     *Read weather data from local storage
     */
    private fun getWeatherFromCache(city: String): Pair<WeatherResponse, Long>? {
        val cachedData = database.weatherEntityQueries.getWeather(city).executeAsOneOrNull()
        return cachedData?.let {
            val weatherResponse = Json.decodeFromString<WeatherResponse>(it.weatherData)
            weatherResponse to it.lastUpdated
        }
    }
    /**
     *Cache weather data in local storage
     */
    private fun cacheWeatherData(city: String, weatherResponse: WeatherResponse) {
        val weatherDataString = Json.encodeToString(weatherResponse)
        database.weatherEntityQueries.insertOrReplaceWeather(
            cityName = city,
            weatherData = weatherDataString,
            lastUpdated = Clock.System.now().toEpochMilliseconds()
        )
    }
}