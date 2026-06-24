# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A Compose Multiplatform library providing scrollbar implementations for platforms where
`androidx.compose.foundation`'s scrollbars are unavailable or unoptimized — notably Android,
where Compose ships no scrollbar at all. Published to Maven Central under the group
`io.github.oikvpqya.compose.fastscroller` (version in `gradle.properties` → `VERSION_NAME`).
Design is inspired by JetBrains' Compose scrollbar and Google's Launcher3 FastScroller.

## Commands

```bash
./gradlew build                                       # build + compile all modules and targets
./gradlew :sample:demo-desktop:run                    # run the desktop demo app
./gradlew :sample:demo-web:wasmJsBrowserDistribution  # build the web demo (deployed to GitHub Pages)
./gradlew :fastscroller-core:assemble                 # build a single library module
./gradlew :sample:demo-android:assembleDebug          # build an Android sample APK
./gradlew publishAllPublicationsToMavenCentral        # publish (CI runs this on tag push)
```

Toolchain: AGP 9.0.1, Gradle 9.5.1, Kotlin 2.3.21, Compose Multiplatform 1.11.1 (all pinned in
`gradle/libs.versions.toml`). AGP 9 requires JDK 17+ to run Gradle. **Note:** AGP 9.0 is not
supported by IntelliJ IDEA — use Android Studio (Otter 3 / 2025.2.3+) for IDE sync.

There are no tests in this repository. Publishing happens via `.github/workflows/publish.yml`
on git tag push; the web demo deploys via `.github/workflows/deploy.yml` (manual dispatch),
building `:sample:demo-web`.

## Module layout

Library modules (published), each multiplatform:

- **`fastscroller-core`** — the scrollbar engine and public API. Depends only on Compose.
- **`fastscroller-material`** — `defaultMaterialScrollbarStyle()` (Material 2). Depends on core.
- **`fastscroller-material3`** — `defaultMaterialScrollbarStyle()` (Material 3). Depends on core.
- **`fastscroller-indicator`** — `IndicatorShape` for FastScroller-style position bubbles.
  Depends only on `compose.ui`, *not* on core.

Sample apps (not published). AGP 9 forbids combining `com.android.application` with the KMP
plugin in one module, so each KMP sample is split into a **shared KMP library** plus
**per-platform application modules**:

- **`sample/android`** — standalone pure-Android app. Uses AGP 9's built-in Kotlin (no
  `kotlin.android` plugin) and `fastscroller-material`.
- **`sample/kmp`** — shared KMP library (`com.android.kotlin.multiplatform.library`); the
  scrollbar-indicator demo. Driven by `sample/kmp-android` (`com.android.application`),
  `sample/kmp-desktop` (`kotlin.jvm` + Compose), and `sample/kmp-web` (wasmJs executable).
- **`sample/demo`** — shared KMP library with the broadest demos (incl. `scrollbar/` expect/actual
  per platform, which stays in the shared lib). Driven by `sample/demo-android`,
  `sample/demo-desktop`, and `sample/demo-web` — the last is what deploys to GitHub Pages.

The app modules each `implementation project(":sample:<name>")` the shared lib; only the
entry points (`MainActivity`, desktop/web `Main.kt`, `AndroidManifest.xml`) live in them. App
namespaces are unique (`...sample.kmp`, `...sample.demo`, etc.) — required by AGP 9's
`android.uniquePackageNames=true` — so their manifests use the fully-qualified activity name.

## The core architectural split: Android vs. skiko

This is the single most important thing to understand. `ScrollbarAdapter` (in
`fastscroller-core`) is an `expect interface` declared in `commonMain`, with two fundamentally
different `actual` implementations:

- **skiko platforms** (jvm/desktop, native/iOS/macOS, js, wasmJs) — `ScrollbarAdapter` is a
  `typealias` to JetBrains' `androidx.compose.foundation.v2.ScrollbarAdapter`, and the factory
  functions just delegate to `androidx.compose.foundation.rememberScrollbarAdapter`. These
  platforms get scrollbar support from upstream Compose Foundation.
- **Android** — Compose Foundation ships no scrollbar adapter, so `androidMain` reimplements it:
  `ScrollbarAdapterAndroid` (interface) plus `ScrollableScrollbarAdapter`,
  `LazyListScrollbarAdapter`, and `LazyGridScrollbarAdapter` under
  `...fastscroller/internal/`. This code is ported from JetBrains' implementation.

The source-set wiring that enables this lives in each library module's `build.gradle.kts`:
`applyDefaultHierarchyTemplate()` is called, then a custom `skikoMain` source set is created
that `dependsOn(commonMain)`, and `jvmMain`/`nativeMain`/`jsMain`/`wasmJsMain` all
`dependsOn(skikoMain)`. So shared non-Android code goes in `src/skikoMain/`, Android-only code in
`src/androidMain/`, and truly common code in `src/commonMain/`. When adding platform behavior,
decide whether it belongs in `skikoMain` (everything but Android) or `androidMain`.

The library modules use the **`com.android.kotlin.multiplatform.library`** plugin (AGP 9's KMP
Android library), so the Android target is configured inside `kotlin { android { namespace;
compileSdk; minSdk } }` (not a top-level `android {}` block or `androidTarget {}`), and it is
single-variant (no `buildTypes`). Targets declared by the library modules: `android`, `jvm`,
`js.browser`, `wasmJs.browser`, `macosArm64`, `iosArm64`, `iosSimulatorArm64`.

## The rendering/interaction layer (commonMain, core)

The public composables `VerticalScrollbar` / `HorizontalScrollbar` (`ScrollbarDsl.kt`) are thin
wrappers that call the internal `Scrollbar` composable (`Scrollbar.kt`). `Scrollbar`:

- uses `SliderAdapter` (`SliderAdapter.kt`) to translate between the adapter's scroll offset
  (a `Double`, intentionally, to support both millions-of-items lists and sub-pixel content) and
  the thumb's pixel position/size;
- lays out the thumb with a custom `MeasurePolicy` (vertical vs. horizontal variants);
- wires interaction through two modifiers: `scrollbarDrag` (`ScrollbarDrag.kt`) for dragging the
  thumb, and `scrollOnPressTrack` (`ScrollOnPressTrack.kt`) for click-on-track scrolling. Which
  one is attached depends on the `enablePressToScroll` parameter.

Styling is data-class based: `ScrollbarStyle` / `ThumbStyle` / `TrackStyle` (`ScrollbarStyle.kt`),
with a theme-free `defaultScrollbarStyle()` in core and theme-aware variants in the material modules.

## Conventions

- All build scripts are Kotlin DSL (`*.gradle.kts`, including `settings.gradle.kts`) — use `=`
  property assignment and `alias(libs.plugins.x)` / `implementation(...)` call syntax.
- Library modules target `jvmToolchain(11)` (Compose 1.11 requires JVM 11+ bytecode); sample
  modules target `jvmToolchain(17)`.
- Dependencies and plugin versions are centralized in `gradle/libs.versions.toml`; reference them
  via `libs.*` aliases rather than hardcoding versions. Compose is depended on by **direct
  coordinates** (`libs.compose.*` → `org.jetbrains.compose.*`), not the deprecated `compose.foundation`
  DSL accessors. Note `compose-material3` is versioned independently (`composeMaterial3`) from the
  rest of Compose (`jetbrainsCompose`). The desktop modules' `compose.desktop.currentOs` is the one
  Compose DSL accessor that is *not* deprecated.
- Each module sets a unique Android `namespace` (e.g. `...fastscroller.material3`) — keep these
  distinct when adding modules (`android.uniquePackageNames=true` enforces it on AGP 9).
- Files ported from AOSP/JetBrains retain the Apache 2.0 copyright header; preserve it when editing
  those files.
