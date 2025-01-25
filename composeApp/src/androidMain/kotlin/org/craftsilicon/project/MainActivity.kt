package org.craftsilicon.project

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.craftsilicon.project.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startKoin {
                androidContext(applicationContext) // Pass the Android context here
                modules(appModule) // Your Koin module
            }
        } catch (e: KoinApplicationAlreadyStartedException) {
            // Catch any Koin initialization errors
            e.printStackTrace()
        }
        enableEdgeToEdge()
        setContent { App() }
    }
}