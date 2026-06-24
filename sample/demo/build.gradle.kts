plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "io.github.oikvpqya.compose.sample.demo.shared"
        compileSdk = 36
        minSdk = 23
    }

    jvm("desktop")

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.kotlinx.coroutines.core)
                implementation(project(":fastscroller-core"))
                implementation(project(":fastscroller-indicator"))
                implementation(project(":fastscroller-material3"))
            }
        }
    }
}
