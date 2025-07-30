package org.craftsilicon.project

import androidx.compose.ui.window.ComposeUIViewController
import org.craftsilicon.project.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

fun MainViewController() = ComposeUIViewController {
    try {
        startKoin {
            modules(appModule, platformModule())
        }
    } catch (e: KoinApplicationAlreadyStartedException) {
       // e.printStackTrace()
    }
    App()
}

