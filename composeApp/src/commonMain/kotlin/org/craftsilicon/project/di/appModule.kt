package org.craftsilicon.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.craftsilicon.project.data.remote.CraftSiliconClient
import org.craftsilicon.project.domain.repository.Repository
import org.craftsilicon.project.presentation.viewmodel.MainViewModel
import org.craftsilicon.project.utils.Constant
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                filter { filter-> filter.url.host.contains("openweathermap.org") }
                sanitizeHeader { header-> header == HttpHeaders.Authorization }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = Constant.TIME_OUT
                connectTimeoutMillis = Constant.TIME_OUT
                socketTimeoutMillis = Constant.TIME_OUT
            }
        }
    }
    single { CraftSiliconClient(get()) }
    single {
        Repository(get())
    }
    singleOf(::MainViewModel)
}