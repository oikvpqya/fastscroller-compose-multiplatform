/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.oikvpqya.compose.fastscroller

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Defines how to scroll the scrollable component and how to display a scrollbar for it.
 *
 * The values of this interface are typically in pixels, but do not have to be.
 * It's possible to create an adapter with any scroll range of `Double` values.
 */
expect interface ScrollbarAdapter {

    // We use `Double` values here in order to allow scrolling both very large (think LazyList with
    // millions of items) and very small (think something whose natural coordinates are less than 1)
    // content.

    /**
     * Scroll offset of the content inside the scrollable component.
     *
     * For example, a value of `100` could mean the content is scrolled by 100 pixels from the
     * start.
     */
    val scrollOffset: Double

    /**
     * The size of the scrollable content, on the scrollable axis.
     */
    val contentSize: Double

    /**
     * The size of the viewport, on the scrollable axis.
     */
    val viewportSize: Double

    /**
     * Instantly jump to [scrollOffset].
     *
     * @param scrollOffset target offset to jump to, value will be coerced to the valid
     * scroll range.
     */
    suspend fun scrollTo(scrollOffset: Double)

}

/**
 * Create and [remember] [ScrollbarAdapter] for
 * scrollable container with the given instance [ScrollState].
 */
@Composable
expect fun rememberScrollbarAdapter(
    scrollState: ScrollState
): ScrollbarAdapter

/**
 * Create and [remember] [ScrollbarAdapter] for
 * lazy scrollable container with the given instance [LazyListState].
 */
@Composable
expect fun rememberScrollbarAdapter(
    scrollState: LazyListState,
): ScrollbarAdapter

/**
 * Create and [remember] [ScrollbarAdapter] for lazy grid with
 * the given instance of [LazyGridState].
 */
@Composable
expect fun rememberScrollbarAdapter(
    scrollState: LazyGridState,
): ScrollbarAdapter

/**
 * ScrollbarAdapter for Modifier.verticalScroll and Modifier.horizontalScroll
 *
 * [scrollState] is instance of [ScrollState] which is used by scrollable component
 */
expect fun ScrollbarAdapter(scrollState: ScrollState): ScrollbarAdapter

/**
 * ScrollbarAdapter for lazy lists.
 *
 * [scrollState] is instance of [LazyListState] which is used by scrollable component
 */
expect fun ScrollbarAdapter(scrollState: LazyListState): ScrollbarAdapter

/**
 * ScrollbarAdapter for lazy grids.
 *
 * [scrollState] is instance of [LazyGridState] which is used by scrollable component
 */
expect fun ScrollbarAdapter(scrollState: LazyGridState): ScrollbarAdapter
