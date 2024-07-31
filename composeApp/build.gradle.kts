import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.9.10"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    jvm("desktop")
    
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
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.bottomSheetNavigator)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.napier)
            implementation(libs.koin.core)
            api(libs.parcelize)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "com.linkandroid"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.linkandroid"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += listOf("mode")
    productFlavors {
        create("pro") {
            dimension = "mode"
            isDefault = true
        }
        create("ui") {
            dimension = "mode"
        }
        create("cold") {
            dimension = "mode"
        }
    }
    buildTypes {
        getByName("release") {
//            signingConfig = signingConfigs.getByName("release")
//            isMinifyEnabled = false
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Mangala Debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)

        debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.38")
        debugImplementation("com.willowtreeapps.hyperion:hyperion-crash:0.9.38")
        releaseImplementation("com.willowtreeapps.hyperion:hyperion-core-no-op:0.9.38")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.linkandroid"
            packageVersion = "1.0.0"
        }
    }
}
