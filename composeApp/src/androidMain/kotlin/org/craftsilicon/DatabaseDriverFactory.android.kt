package org.craftsilicon

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope


lateinit var appContext: Context

fun initDatabaseContext(context: Context) {
    appContext = context.applicationContext
}


//actual fun Scope.sqlDriverFactory(): SqlDriver {
//    return AndroidSqliteDriver(
//        CraftSilliconDb.Schema,
//        androidContext(),
//        "SQLDelightDemoDatabase.db"
//    )
//}



actual suspend fun provideDbDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver {
    return AndroidSqliteDriver(schema.synchronous(),  appContext, "SQLDelightDemoDatabase.db")
}
