plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvmToolchain(17)

    applyDefaultHierarchyTemplate()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    js(IR) {
        browser()
    }

    wasmJs {
        browser()
    }

    macosX64()
    macosArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "fastscroller-indicator"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(compose.ui)
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = "io.github.oikvpqya.compose.fastscroller"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
