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
        /**
         *Android context required by Koin
         */
        try {
            startKoin {
                androidContext(applicationContext)
                modules(appModule)
            }
        } catch (e: KoinApplicationAlreadyStartedException) {
            e.printStackTrace()
        }
        enableEdgeToEdge()
        setContent { App() }
    }
}