package org.craftsilicon

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.core.scope.Scope

actual fun Scope.sqlDriverFactory(): SqlDriver {
    return NativeSqliteDriver(
        CraftSilliconDb.Schema,
        "SQLDelightDemoDatabase.db"
    )
}