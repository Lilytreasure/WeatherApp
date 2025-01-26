package org.craftsilicon

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.sqlDriverFactory(): SqlDriver {
    return AndroidSqliteDriver(
        CraftSilliconDb.Schema,
        androidContext(),
        "SQLDelightDemoDatabase.db"
    )
}