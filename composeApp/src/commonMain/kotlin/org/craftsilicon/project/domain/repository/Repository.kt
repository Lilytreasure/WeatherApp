package org.craftsilicon.project.domain.repository

import org.craftsilicon.project.data.remote.CraftSiliconClient
import org.craftsilicon.project.data.repository.CraftSiliconApi
import org.craftsilicon.project.domain.model.weather.WeatherResponse

class Repository(
    private val cryptoClient: CraftSiliconClient
) : CraftSiliconApi {

    override suspend fun getWeatherForecast(
        city: String,
        apiKey: String,
        units: String
    ): WeatherResponse {
        return cryptoClient.getWeatherForecast(city, apiKey, units)
    }


    //Cache

//    override suspend fun getWeatherForecast2(
//        city: String,
//        apiKey: String,
//        units: String
//    ): WeatherResponse {
//        return try {
//            // Attempt to fetch weather data from the API
//            val response = cryptoClient.getWeatherForecast(city, apiKey, units)
//
//            // Cache the weather data in the database
//            weatherDatabase.insertWeather(
//                city = city,
//                temperature = response.main.temp,
//                description = response.weather[0].description,
//                timestamp = System.currentTimeMillis()
//            )
//
//            response
//        } catch (e: Exception) {
//            // If API call fails, try fetching from the local cache
//            val cachedWeather = weatherDatabase.getWeatherByCity(city)
//            cachedWeather?.let {
//                // Return the cached data if available
//                WeatherResponse(
//                    main = Main(temp = it.temperature),
//                    weather = listOf(Weather(description = it.description))
//                )
//            } ?: throw e // Throw the exception if no cached data exists
//        }

}