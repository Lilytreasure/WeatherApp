package org.craftsilicon.project.presentation.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import org.craftsilicon.project.domain.model.weather.WeatherResponse
import org.craftsilicon.project.domain.usecase.ResultState
import org.craftsilicon.project.presentation.ui.components.AlertDialogExample
import org.craftsilicon.project.presentation.ui.components.LoadingBox
import org.craftsilicon.project.presentation.viewmodel.MainViewModel
import org.craftsilicon.project.theme.LocalThemeIsDark
import org.craftsilicon.project.utils.extractErrorMessage
import org.craftsilicon.project.utils.filterAndGroupWeatherData
import org.craftsilicon.project.utils.formatTimeWithoutDateTimeFormatter
import org.craftsilicon.project.utils.toFormattedDateTime
import org.craftsilicon.project.utils.toLocalDateTime
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    viewModel: MainViewModel = koinInject(),
) {
    var isDark by LocalThemeIsDark.current
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    //Todo-- Handle location locale to pick location automatically
    var queryText by remember { mutableStateOf("Nairobi") }
    var lastupdate by remember { mutableStateOf(0L) }
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() {
        refreshScope.launch {
            if (queryText.isNotBlank()) {
            }
            viewModel.getWeatherForecast(cityName = queryText)
            delay(1500)
            refreshing = false
        }
    }

    var showError by remember { mutableStateOf("") }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    LaunchedEffect(Unit) {
        viewModel.getWeatherForecast(cityName = queryText)
    }
    val latestWeather by viewModel.weather.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    when (latestWeather) {
        is ResultState.ERROR -> {
            val error = (latestWeather as ResultState.ERROR).message
            val formattedError = extractErrorMessage(error)
            showError = formattedError
            openAlertDialog.value = true
        }

        is ResultState.LOADING -> {
            LoadingBox()
        }

        is ResultState.SUCCESS -> {
            val weatherdata = (latestWeather as ResultState.SUCCESS).response
            weatherData = weatherdata.first
            val lastUpdated = weatherdata.second
            lastupdate = lastUpdated
        }

        is ResultState.EMPTY -> {
        }
    }
    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = {
                    openAlertDialog.value = false
                    viewModel.resetWeatherState()
                    queryText = ""
                },
                dialogText = showError,
                icon = Icons.Outlined.Info
            )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Weather App",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            TextField(
                value = queryText,
                singleLine = true,
                onValueChange = {
                    queryText = it
                },
                placeholder = { Text(text = "Search City name") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                viewModel.getWeatherForecast(cityName = queryText)
                            }
                    )

                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(40.dp)
            )
            weatherData?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "City: ${data.city.name}",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 10.dp, bottom = 7.dp)
                        )
                        Text(
                            text = "Last update: ${lastupdate.toFormattedDateTime()}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 10.dp, bottom = 7.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "Current Temp: ${data.list.firstOrNull()?.main?.temp ?: "N/A"} °C",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 7.dp)
                    )
                    Text(
                        text = "Description: ${
                            data.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
            weatherData?.let { data ->
                val groupedWeather = filterAndGroupWeatherData(data)
                groupedWeather.forEach { (day, weatherItems) ->
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = day,  // Display day (e.g., "Monday")
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            weatherItems.forEach { weatherItem ->
                                val dateTime =
                                    weatherItem.dt_txt.toLocalDateTime(TimeZone.currentSystemDefault())
                                val time = formatTimeWithoutDateTimeFormatter(dateTime)
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.padding(end = 10.dp)) {
                                            Text(
                                                text = "Time: $time",
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                            Text(
                                                text = "Temp: ${weatherItem.main.temp}°C",
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )

                                        }
//                                        Text(
//                                            text = " ${weatherItem.weather.firstOrNull()?.description}",
//                                            style = MaterialTheme.typography.bodySmall,
//                                            modifier = Modifier.padding(bottom = 2.dp)
//                                        )
                                    }
                                }
                            }
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











