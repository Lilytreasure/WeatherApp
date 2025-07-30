package org.craftsilicon.project

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.craftsilicon.project.di.CraftSiliconDatabaseWrapper
import org.koin.core.module.Module
import org.koin.dsl.module


class WebPlatform : Platform {
    override val name: String = "Web"
}
actual fun getPlatform(): Platform = WebPlatform()

actual fun platformModule() = module {
    single<SqlDriver> {
        createDefaultWebWorkerDriver()
    }
    single {
        CraftSilliconDb(get())
    }
    single {
        CraftSiliconDatabaseWrapper(get(), get())
    }
}