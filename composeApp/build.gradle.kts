import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

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

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.compose.ui.test.junit4.android)
                debugImplementation(libs.compose.ui.test.manifest)
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

    //Generating BuildConfig for multiplatform
    val buildConfigGenerator by tasks.registering(Sync::class) {
        val packageName = "secrets"
        val secretProperties = readPropertiesFromFile("secrets.properties")
        from(
            resources.text.fromString(
                """
                |package $packageName
                |
                |object BuildConfig {
                |  const val API_KEY = "${secretProperties.getPropertyValue("API_KEY")}"
                |  const val CHANNEL_ID = "${secretProperties.getPropertyValue("CHANNEL_ID")}"
                |  const val RESULTS = "${secretProperties.getPropertyValue("RESULTS")}"
                |  const val SIMPLE_RESULTS = "${secretProperties.getPropertyValue("SIMPLE_RESULTS")}"
                |}
                |
                """.trimMargin()
            )
        )
        {
            rename { "BuildConfig.kt" } // set the file name
            into(packageName) // change the directory to match the package
        }
        into(layout.buildDirectory.dir("generated-secret-keys/kotlin/"))
    }


    sourceSets {
        // convert the task to a file-provider for a secret keys file
        commonMain{
            kotlin.srcDir(buildConfigGenerator.map { it.destinationDir })
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

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
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.cmpcharts)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-annotations-common"))
            implementation(libs.assertk)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
    }
}

android {
    namespace = "half.mouth.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "half.mouth.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
        getByName("debug") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
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
    ksp(libs.room.compiler)
}


/***   Read from file name secret.properties  ***/
fun readPropertiesFromFile(fileName: String): Map<String, String> {
    val parts = fileName.split('.')
    val localPropertyFileName = if (parts.size >= 2) {
        val nameWithoutExtension = parts.dropLast(1).joinToString(".")
        val extension = parts.last()
        "$nameWithoutExtension.local.$extension"
    } else {
        fileName
    }
    val isLocalFileExists= File(project.rootDir,localPropertyFileName).exists()
    val fileContent = try {
        File(project.rootDir,if (isLocalFileExists) localPropertyFileName else fileName).readText()
    } catch (e: Exception) {
        e.printStackTrace()
        return emptyMap()
    }

    val properties = mutableMapOf<String, String>()

    fileContent.lines().forEach { line ->
        val keyValuePair = line.split('=')
        if (keyValuePair.size == 2) {
            properties[keyValuePair[0].trim()] = keyValuePair[1]
        }
    }
    return properties
}

/** If System.env value exists it will be returned value which can be useful for CI/CD pipeline **/
fun Map<String, String>.getPropertyValue(key: String): String? {
    val envValue = System.getenv(key)
    if (envValue.isNullOrEmpty().not()) return envValue
    return this.getOrDefault(key, null)
}
