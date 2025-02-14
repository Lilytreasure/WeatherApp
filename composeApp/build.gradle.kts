import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
}

val localProperties = Properties()
val file = rootProject.file("local.properties")
if (file.exists()) {
    localProperties.load(file.inputStream())
}

val apiKey = localProperties.getProperty("API_KEY", "").trim()

val generatedDir = layout.buildDirectory.dir("generated/source/apiKeys")

tasks.register("generateApiKey") {
    val outputDir = generatedDir.get().asFile
    val packageDir = File(outputDir, "org/craftsilicon/project")
        val file = File(packageDir, "ApiKeys.kt")

    doLast {
        packageDir.mkdirs()

        file.writeText(
            """
            package org.craftsilicon.project


            object ApiKeys {
                const val API_KEY = "$apiKey"
            }
            """.trimIndent()
        )
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs(generatedDir.get().asFile.absolutePath)
        }
    }
}
/**
 *  Ensure API key generation runs before compilation
 */
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn("generateApiKey")
}



kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = false
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            //Added
            implementation(compose.uiTooling)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            //Android Local cache
            implementation(libs.sqlDelight.driver.android)
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.0.0"))
            implementation(libs.io.insert.koin.koin.core)
            implementation(libs.koin.android)
            implementation(libs.koin.annotations)

        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            //Added
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            //Navigation
            implementation(libs.voyager.navigator)
            implementation(libs.tab.navigator)
            //Logger
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            //Viewmodel
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.content.negociation)
            implementation(libs.ktor.client.serialization.json)
            implementation(libs.ktor.client.logging)
            //Serializer
            implementation(libs.kotlinx.serialization.json)
            //Date/Time
            implementation(libs.kotlinx.datetime)
            //Adaptive screen size
            implementation(libs.screen.size)
            //Koin -Dependency Injection
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.0.0"))
            implementation(libs.insert.koin.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
            //camel image loader
            implementation(libs.kamel.image)
            implementation(libs.kamel.image.default)
            implementation(libs.bignum)
            implementation(libs.ktor.core)
            //coil image loader and cache
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            //SQl Delight cache
            api(libs.sqlDelight.coroutinesExtensions)
            api(libs.sqlDelight.primitiveAdapters)
            //Conectivity
            implementation("com.plusmobileapps:konnectivity:0.1-alpha01")

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            //iOS Local cache
            implementation(libs.sqlDelight.driver.native)
        }
    }
}

android {
    namespace = "org.craftsilicon.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.craftsilicon.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

sqldelight {
    databases {
        create("CraftSilliconDb") {
            packageName.set("org.craftsilicon.project.db")
        }
    }
}

