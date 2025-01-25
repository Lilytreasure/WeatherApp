package org.craftsilicon.project.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.craftsilicon.project.domain.model.weather.WeatherItem
import org.craftsilicon.project.domain.model.weather.WeatherResponse

fun String.toLocalDateTime(timeZone: TimeZone): LocalDateTime {
    val dateTimeParts = this.split(" ")
    val date = dateTimeParts[0]
    val time = dateTimeParts[1]
    return LocalDateTime.parse("${date}T$time")
}



fun filterAndGroupWeatherData(weatherResponse: WeatherResponse): Map<String, List<WeatherItem>> {
    val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    return weatherResponse.list.groupBy { item ->
        val dateTime = item.dt_txt.toLocalDateTime(TimeZone.UTC)
        dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    }.filterKeys { dayName ->
        dayName in weekdays
    }
}