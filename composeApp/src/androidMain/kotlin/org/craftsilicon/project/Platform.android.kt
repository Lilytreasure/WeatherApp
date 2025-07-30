package org.craftsilicon.project

import android.os.Build
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.craftsilicon.project.di.CraftSiliconDatabaseWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun platformModule() = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            CraftSilliconDb.Schema.synchronous(),
            androidContext(), // requires androidContext()
            "SQLDelightDemoDatabase.db"
        )
    }
    single {
        CraftSilliconDb(get()) // SqlDriver injected here
    }
    single {
        CraftSiliconDatabaseWrapper(get(), get()) // wrapper uses driver + DB
    }
}