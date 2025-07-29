package org.craftsilicon

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.craftsilicon.project.db.CraftSilliconDb
import org.koin.core.scope.Scope
import org.w3c.dom.Worker

actual fun Scope.sqlDriverFactory(): SqlDriver {
    return WebWorkerDriver(
        Worker(
            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
        )
    ).also {  CraftSilliconDb.Schema}
}