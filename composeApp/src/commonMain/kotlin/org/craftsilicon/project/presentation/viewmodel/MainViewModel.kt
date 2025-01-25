package org.craftsilicon.project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.craftsilicon.project.domain.model.weather.WeatherResponse
import org.craftsilicon.project.domain.repository.Repository
import org.craftsilicon.project.domain.usecase.ResultState

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _weather = MutableStateFlow<ResultState<WeatherResponse>>(ResultState.LOADING)
    var weather: StateFlow<ResultState<WeatherResponse>> = _weather.asStateFlow()
    fun getWeatherForecast(cityName: String){
        viewModelScope.launch {
            _weather.value = ResultState.LOADING
            try {
                val response = repository.getWeatherForecast(city = cityName,
                    apiKey = "cfe577b09f43deea2722462eea76e473",
                    units ="metric"
                )
                _weather.value = ResultState.SUCCESS(response)
                println("Weather loaded " + response)
            }catch (e:Exception){
                _weather.value = ResultState.ERROR(e.message.toString())
            }
        }
    }
}