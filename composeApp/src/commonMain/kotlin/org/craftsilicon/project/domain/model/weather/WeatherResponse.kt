package org.craftsilicon.project.domain.model.weather
import kotlinx.serialization.Serializable


/**
 *Important to set default values(encodeDefault) to avoid serialization failure
 */

@Serializable
data class WeatherResponse(
    val cod: String = "",
    val message: Int = 0,
    val cnt: Int = 0,
    val list: List<WeatherItem> = emptyList(),
    val city: City = City()
)

@Serializable
data class WeatherItem(
    val dt: Long = 0L,
    val main: Main = Main(),
    val weather: List<Weather> = emptyList(),
    val clouds: Clouds = Clouds(),
    val wind: Wind = Wind(),
    val visibility: Int = 0,
    val pop: Double = 0.0,
    val sys: Sys = Sys(),
    val dt_txt: String = "",
    val rain: Rain? = null // Optional, default is null
)

@Serializable
data class Main(
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val temp_min: Double = 0.0,
    val temp_max: Double = 0.0,
    val pressure: Int = 0,
    val sea_level: Int = 0,
    val grnd_level: Int = 0,
    val humidity: Int = 0,
    val temp_kf: Double = 0.0
)

@Serializable
data class Weather(
    val id: Int = 0,
    val main: String = "",
    val description: String = "",
    val icon: String = ""
)

@Serializable
data class Clouds(
    val all: Int = 0
)

@Serializable
data class Wind(
    val speed: Double = 0.0,
    val deg: Int = 0,
    val gust: Double = 0.0
)

@Serializable
data class Sys(
    val pod: String = ""
)

@Serializable
data class Rain(
    val `3h`: Double? = null // Optional, default is null
)

@Serializable
data class City(
    val id: Int = 0,
    val name: String = "",
    val coord: Coord = Coord(),
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Long = 0L,
    val sunset: Long = 0L
)

@Serializable
data class Coord(
    val lat: Double = 0.0,
    val lon: Double = 0.0
)



