package org.craftsilicon.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window
import org.craftsilicon.project.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException
import org.w3c.dom.get

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val supportsNotifications = window["supportsNotifications"] as? Boolean ?: false
    try {
        startKoin {
            modules(appModule, platformModule())
        }
    } catch (e: KoinApplicationAlreadyStartedException) {
        // Ignore if already started
        println("Koin Errror;;;;;;;"+e)
    }
        ComposeViewport("WeatherApp") {
            App()
        }
}
