package org.craftsilicon.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.craftsilicon.project.domain.model.weather.WeatherResponse
import org.craftsilicon.project.utils.Constant.BASE_URL

class CraftSiliconClient(private val client: HttpClient) {
    suspend fun getWeatherForecast(city: String, apiKey: String, units: String = "metric"): WeatherResponse {
        return client.get(BASE_URL) {
            parameter("q", city)
            parameter("appid", apiKey)
            parameter("units", units)
        }.body()
    }
}