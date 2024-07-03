import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.animation)
            implementation(libs.firebase.auth)
            implementation(libs.navigation.compose)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.multiplatformSettings.noargs)
            implementation(libs.bundles.ktor)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlin.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.logback.classic)
            implementation(libs.kotlinx.datetime)
            implementation(libs.firebase.database)
            implementation(libs.qr.kit)
            implementation(libs.chart)
            implementation(libs.konnection)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "half.mouth.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "half.mouth.app"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    commonMainApi(libs.mvvm.compose) // api mvvm-core, getViewModel for Compose Multiplatform
    commonMainApi(libs.moko.mvvm.flow.compose) // api mvvm-flow, binding extensions for Compose Multiplatform
    commonMainApi(libs.moko.mvvm.livedata.compose) // api mvvm-livedata, binding extensions for Compose Multiplatform
    ksp(libs.room.compiler)
}
