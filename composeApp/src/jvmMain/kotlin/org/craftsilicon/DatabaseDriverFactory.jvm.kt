package org.craftsilicon

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.core.scope.Scope
import java.util.Properties

actual fun Scope.sqlDriverFactory(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(  "jdbc:sqlite:SQLDelightDemoDatabase.db", Properties(),  CraftSilliconDb.Schema)
    return driver
}