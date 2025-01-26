package org.craftsilicon.project.data.repository

import org.craftsilicon.project.domain.model.weather.WeatherResponse

interface CraftSiliconApi {
    suspend fun getWeatherForecast(
        city: String,
        apiKey: String,
        units: String = "metric"
    ): Pair<WeatherResponse, Long>

}