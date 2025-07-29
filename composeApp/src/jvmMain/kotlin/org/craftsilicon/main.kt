package org.craftsilicon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.craftsilicon.project.App
import org.craftsilicon.project.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

fun main() {
    try {
        startKoin {
            modules(appModule)
        }
    } catch (e: KoinApplicationAlreadyStartedException) {
        // Ignore if already started
        println("Koin Errror;;;;;;;"+e)
    }
    System.setProperty("apple.awt.application.appearance", "system")

    println("started;;;;;;;;;;;;;;")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "WeatherApp",
            alwaysOnTop = true,
            state = rememberWindowState(width = 600.dp, height = 800.dp)
        ) {
            App()
        }
    }
}
