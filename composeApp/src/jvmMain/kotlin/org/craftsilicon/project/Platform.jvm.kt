package org.craftsilicon.project

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.craftsilicon.project.di.CraftSiliconDatabaseWrapper
import org.koin.dsl.module

class JvmPlatform : Platform {
    override val name: String = "Jvm"
}

actual fun getPlatform(): Platform = JvmPlatform()
actual fun platformModule() = module {
    single<SqlDriver> {
        JdbcSqliteDriver(
            url = "jdbc:sqlite:SQLDelightDemoDatabase.db"
        ).also { CraftSilliconDb.Schema.create(it) }
    }
    single { CraftSilliconDb(get()) }
    single { CraftSiliconDatabaseWrapper(get(), get()) }
}