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
        namespace = "io.github.oikvpqya.compose.fastscroller.material3"
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
            baseName = "fastscroller-material3"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(project(":fastscroller-core"))
            }
        }
    }
}
