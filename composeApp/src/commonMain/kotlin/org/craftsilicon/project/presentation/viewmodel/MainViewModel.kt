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
import org.craftsilicon.project.utils.Constant

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _weather = MutableStateFlow<ResultState<Pair<WeatherResponse, Long>>>(ResultState.LOADING)
    var weather: StateFlow<ResultState<Pair<WeatherResponse, Long>>> = _weather.asStateFlow()
    fun getWeatherForecast(cityName: String){
        viewModelScope.launch {
            _weather.value = ResultState.LOADING
            try {
                val response = repository.getWeatherForecast(city = cityName,
                    apiKey = Constant.API_KEY,
                    units ="metric"
                )
                _weather.value = ResultState.SUCCESS(response)
                println("Error1;;;;"+ _weather.value.toString())
            }catch (e:Exception){
                _weather.value = ResultState.ERROR(e.message.toString())
                println("Error2;;;;"+ _weather.value.toString())
            }
        }
    }
    /**
     *Fall back to default state
     */
    fun resetWeatherState() {
        _weather.value = ResultState.EMPTY
    }
}