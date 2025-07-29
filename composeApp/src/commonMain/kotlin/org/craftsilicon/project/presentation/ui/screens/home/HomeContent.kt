package org.craftsilicon.project.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import kotlinx.datetime.todayIn
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
            .background(MaterialTheme.colorScheme.background)
            .pullRefresh(state = refreshState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Weather App",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            SearchView(
                queryText = queryText,
                onQueryTextChange = { queryText = it },
                onSearchClick = { viewModel.getWeatherForecast(cityName = queryText) },
                isDark = isDark
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
            ) {
                weatherData?.let { data ->

                    val today =
                        Clock.System.todayIn(TimeZone.currentSystemDefault())  // Get today's date
                    val groupedWeather = filterAndGroupWeatherData(data)
                    var todayDisplayed = false
                    groupedWeather.forEach { (day, weatherItems) ->
                        val dateTimeCheck =
                            weatherItems.firstOrNull()?.dt_txt?.toLocalDateTime(TimeZone.currentSystemDefault())
                        if (dateTimeCheck?.date == today && !todayDisplayed) {
                            todayDisplayed = true
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {
                                Box(modifier = Modifier.padding(5.dp)) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = data.city.name,
                                                style = MaterialTheme.typography.titleLarge,
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    bottom = 7.dp
                                                )
                                            )
                                            Text(
                                                text = "Last update: ${lastupdate.toFormattedDateTime()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    bottom = 7.dp
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = "Today",
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            weatherItems.forEach { weatherItem ->
                                                val dateTime =
                                                    weatherItem.dt_txt.toLocalDateTime(TimeZone.currentSystemDefault())
                                                val timeFormat =
                                                    formatTimeWithoutDateTimeFormatter(dateTime)
                                                item {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Column(modifier = Modifier.padding(end = 10.dp)) {
                                                            Text(
                                                                text = "Time: $timeFormat",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                modifier = Modifier.padding(bottom = 2.dp)
                                                            )
                                                            Text(
                                                                text = "Temp: ${weatherItem.main.temp}°C",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                modifier = Modifier.padding(bottom = 2.dp)
                                                            )
                                                        }
                                                        val iconCode =
                                                            weatherItem.weather.firstOrNull()?.icon.orEmpty()
                                                        val iconUrl =
                                                            "https://openweathermap.org/img/wn/${iconCode}@2x.png"
                                                        val painter =
                                                            asyncPainterResource(data = iconUrl)
                                                        KamelImage(
                                                            { painter },
                                                            contentDescription = "Profile",
                                                            modifier = Modifier
                                                                .size(70.dp),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (dateTimeCheck?.date != today) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                            ) {
                                Text(
                                    text = day,  // Display day (e.g., "Monday")
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                                        .padding(10.dp)

                                ) {
                                    weatherItems.forEach { weatherItem ->
                                        val dateTime =
                                            weatherItem.dt_txt.toLocalDateTime(TimeZone.currentSystemDefault())
                                        val time = formatTimeWithoutDateTimeFormatter(dateTime)
                                        item {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
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
                                                val iconCode =
                                                    weatherItem.weather.firstOrNull()?.icon.orEmpty()
                                                val iconUrl =
                                                    "https://openweathermap.org/img/wn/${iconCode}@2x.png"
                                                val painter = asyncPainterResource(data = iconUrl)
                                                KamelImage(
                                                    { painter },
                                                    contentDescription = "Profile",
                                                    modifier = Modifier
                                                        .size(65.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
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

@Composable
fun SearchView(
    queryText: String,
    onQueryTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    TextField(
        value = queryText,
        singleLine = true,
        onValueChange = onQueryTextChange,
        placeholder = {
            Text(
                text = "Search City",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { onSearchClick() }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = if (isDark) Color.White else Color.Black,
            unfocusedTextColor = if (isDark) Color.White else Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(40.dp),
        textStyle = MaterialTheme.typography.bodySmall
    )
}












