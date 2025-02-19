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
            baseName = "fastscroller-core"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(compose.animation)
                api(compose.foundation)
                api(compose.runtime)
                api(compose.ui)
                api(libs.kotlinx.coroutines.core)
            }
        }

        val skikoMain by creating {
            dependsOn(commonMain.get())
        }

        listOf(
            jvmMain,
            nativeMain,
            jsMain,
            wasmJsMain,
        ).forEach { target ->
            target {
                dependsOn(skikoMain)
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = "io.github.oikvpqya.compose.fastscroller"

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
