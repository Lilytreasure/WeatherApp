package org.craftsilicon

import app.cash.sqldelight.db.SqlDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.core.scope.Scope

fun createDatabase(driver: SqlDriver): CraftSilliconDb {
    return CraftSilliconDb(
        driver = driver,
    )
}
expect fun Scope.sqlDriverFactory(): SqlDriver