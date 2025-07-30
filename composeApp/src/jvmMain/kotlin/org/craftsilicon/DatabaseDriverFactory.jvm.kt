package org.craftsilicon

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

//actual fun Scope.sqlDriverFactory(): SqlDriver {
//    val driver: SqlDriver = JdbcSqliteDriver(  "jdbc:sqlite:SQLDelightDemoDatabase.db", Properties(),  CraftSilliconDb.Schema)
//    return driver
//}

actual suspend fun provideDbDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver {
    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { schema.create(it).await() }
}
