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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines visual style of scrollbars (thickness, shapes, colors, etc).
 * Can be passed as a parameter of scrollbar.
 */
@Immutable
data class ScrollbarStyle(
    val minimalHeight: Dp,
    val thickness: Dp,
    val hoverDurationMillis: Int,
    val thumbStyle: ThumbStyle,
    val trackStyle: TrackStyle,
)

/**
 * Defines visual style of scrollbar thumb.
 * Can be passed as a parameter of scrollbar.
 */
@Immutable
data class ThumbStyle(
    val shape: Shape,
    val unhoverColor: Color,
    val hoverColor: Color,
)

/**
 * Defines visual style of scrollbar track.
 * Can be passed as a parameter of scrollbar.
 */
@Immutable
data class TrackStyle(
    val shape: Shape,
    val unhoverColor: Color,
    val hoverColor: Color,
)

/**
 * Simple default [ScrollbarStyle] without applying MaterialTheme.
 */
fun defaultScrollbarStyle() = ScrollbarStyle(
    minimalHeight = 16.dp,
    thickness = 8.dp,
    hoverDurationMillis = 300,
    thumbStyle = ThumbStyle(
        shape = RoundedCornerShape(4.dp),
        unhoverColor = Color.Black.copy(alpha = 0.12f),
        hoverColor = Color.Black.copy(alpha = 0.50f),
    ),
    trackStyle = TrackStyle(
        shape = RectangleShape,
        unhoverColor = Color.Transparent,
        hoverColor = Color.Transparent,
    ),
)
