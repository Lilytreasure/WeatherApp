package org.craftsilicon

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import org.craftsilicon.project.db.CraftSilliconDb

fun createDatabase(driver: SqlDriver): CraftSilliconDb {
    return CraftSilliconDb(
        driver = driver,
    )
}


//expect fun Scope.sqlDriverFactory(): SqlDriver

expect suspend fun provideDbDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver

