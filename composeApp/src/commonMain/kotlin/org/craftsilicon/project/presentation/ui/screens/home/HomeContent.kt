package org.craftsilicon.project.presentation.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.craftsilicon.project.domain.model.weather.WeatherResponse
import org.craftsilicon.project.domain.usecase.ResultState
import org.craftsilicon.project.presentation.ui.components.ErrorBox
import org.craftsilicon.project.presentation.ui.components.LoadingBox
import org.craftsilicon.project.presentation.viewmodel.MainViewModel
import org.craftsilicon.project.theme.LocalThemeIsDark
import org.craftsilicon.project.utils.filterAndGroupWeatherData
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    viewModel: MainViewModel = koinInject(),
) {
    var isDark by LocalThemeIsDark.current
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var queryText by remember { mutableStateOf("Nairobi") }
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() {
        refreshScope.launch {
            viewModel.getWeatherForecast(cityName = queryText)
            delay(1500)
            refreshing = false
        }
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    LaunchedEffect(Unit) {
        viewModel.getWeatherForecast(cityName =queryText )
    }
    val latestWeather by viewModel.weather.collectAsState()

    when (latestWeather) {
        is ResultState.ERROR -> {
            val error = (latestWeather as ResultState.ERROR).message
            ErrorBox(error)
        }
        is ResultState.LOADING -> {
            LoadingBox()
        }

        is ResultState.SUCCESS -> {
            val weatherdata = (latestWeather as ResultState.SUCCESS).response
            weatherData = weatherdata
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(state = refreshState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = queryText,
                singleLine = true,
                onValueChange = {
                    queryText = it
                },
                placeholder = { Text(text = "Search City") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable{
                                viewModel.getWeatherForecast(cityName = queryText)
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(40.dp)
            )
            weatherData?.let { data ->
                // Displaying weather info
                Text(
                    text = "City: ${data.city.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Current Temp: ${data.list.firstOrNull()?.main?.temp ?: "N/A"} °C",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Description: ${
                        data.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
                val groupedWeather = filterAndGroupWeatherData(data)
                groupedWeather.forEach { (day, weatherItems) ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = day,  // Display day (e.g., "Monday")
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        weatherItems.forEach { weatherItem ->
                            Text(
                                text = "Time: ${weatherItem.dt_txt}, Temp: ${weatherItem.main.temp}°C",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "Description, : ${weatherItem.weather.firstOrNull()?.description}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}



