
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.realm)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
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

            //https://github.com/DevSrSouza/compose-icons
            implementation(libs.simple.icons)

            //https://github.com/adrielcafe/voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.kodein)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.bottomSheetNavigator)

            //https://github.com/Kotlin/kotlinx.coroutines
            implementation(libs.kotlinx.coroutines.core)

            //https://github.com/realm/realm-kotlin?tab=readme-ov-file
            implementation(libs.realm.base)

            //https://github.com/icerockdev/moko-permissions?tab=readme-ov-file#installation
            //https://medium.com/@marceloamendes/como-utilizar-o-moko-media-e-o-moko-permissions-no-compose-multiplatform-d576cf5cda70
            // Moko Permissions
            implementation(libs.permissions)
            // Moko Permissions Compose
            implementation(libs.permissions.compose)

            // kmp notification if needed https://github.com/mirzemehdi/KMPNotifier
            // kmp NativeCoroutines if needed https://github.com/rickclephas/KMP-NativeCoroutines

            //kodein https://github.com/kosi-libs/Kodein
            implementation(libs.kodein.di)
            implementation(libs.kodein.di.compose)

            //https://github.com/cashapp/molecule
            implementation(libs.molecule.runtime)

            //https://github.com/Kotlin/kotlinx-datetime
            implementation(libs.kotlinx.datetime)

            //network data time https://github.com/softartdev/Kronos-Multiplatform


            //https://github.com/WojciechOsak/Calendar
            implementation(libs.calendar)


        }
    }
}

android {
    namespace = "com.hamidrezabashiri.calendar"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.hamidrezabashiri.calendar"
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
}

dependencies {
    debugImplementation(compose.uiTooling)
}

