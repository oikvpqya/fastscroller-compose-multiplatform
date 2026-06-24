pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // See https://splitties.github.io/refreshVersions/
    id("de.fayard.refreshVersions") version "0.60.6"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel.isLessStableThan(current.stabilityLevel)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "fastscroller-compose-multiplatform"

include(":fastscroller-core")
include(":fastscroller-indicator")
include(":fastscroller-material")
include(":fastscroller-material3")
include(":sample:android")
include(":sample:kmp")
include(":sample:kmp-android")
include(":sample:kmp-desktop")
include(":sample:kmp-web")
include(":sample:demo")
include(":sample:demo-android")
include(":sample:demo-desktop")
include(":sample:demo-web")
