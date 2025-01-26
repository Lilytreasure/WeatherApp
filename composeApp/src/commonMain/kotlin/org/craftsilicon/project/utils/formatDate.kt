package org.craftsilicon.project.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.craftsilicon.project.domain.model.weather.WeatherItem
import org.craftsilicon.project.domain.model.weather.WeatherResponse

fun String.toLocalDateTime(timeZone: TimeZone): LocalDateTime {
    val dateTimeParts = this.split(" ")
    val date = dateTimeParts[0]
    val time = dateTimeParts[1]
    return LocalDateTime.parse("${date}T$time")
}

fun formatTimeWithoutDateTimeFormatter(localDateTime: LocalDateTime): String {
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    val second = localDateTime.second.toString().padStart(2, '0')
    return "$hour:$minute:$second"
}

fun filterAndGroupWeatherData(weatherResponse: WeatherResponse): Map<String, List<WeatherItem>> {
    return weatherResponse.list.groupBy { item ->
        val dateTime = item.dt_txt.toLocalDateTime(TimeZone.currentSystemDefault())
        dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun extractErrorMessage(fullError: String): String {
    return try {
        val regex = Regex("\"message\":\"(.*?)\"")
        regex.find(fullError)?.groupValues?.get(1)
            ?: "An unknown error occurred check your internet connection"
    } catch (e: Exception) {
        "An unknown error occurred check your internet connection"
    }
}

fun Long.toFormattedDateTime(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.date} ${
        localDateTime.hour.toString().padStart(2, '0')
    }:${localDateTime.minute.toString().padStart(2, '0')}:${
        localDateTime.second.toString().padStart(2, '0')
    }"
}