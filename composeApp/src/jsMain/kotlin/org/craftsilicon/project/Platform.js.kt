package org.craftsilicon.project



class WebPlatform : Platform {
    override val name: String = "Web"
}
actual fun getPlatform(): Platform = WebPlatform()