package org.craftsilicon.project

class JvmPlatform : Platform {
    override val name: String = "Jvm"
}

actual fun getPlatform(): Platform = JvmPlatform()