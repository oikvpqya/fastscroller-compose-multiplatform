plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvmToolchain(11)

    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.oikvpqya.compose.fastscroller"
        compileSdk = 36
        minSdk = 23
    }

    jvm()

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    macosArm64()

    listOf(
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
                api(libs.compose.animation)
                api(libs.compose.foundation)
                api(libs.compose.runtime)
                api(libs.compose.ui)
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
