package org.craftsilicon.project

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.craftsilicon.project.di.CraftSiliconDatabaseWrapper
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun platformModule() = module{
    single<SqlDriver> {
        NativeSqliteDriver(
            CraftSilliconDb.Schema.synchronous(),
            "SQLDelightDemoDatabase.db"
        )
    }
    single { CraftSilliconDb(get()) }
    single { CraftSiliconDatabaseWrapper(get(), get()) }
}