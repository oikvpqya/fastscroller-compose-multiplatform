package io.github.oikvpqya.compose.fastscroller.material

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import io.github.oikvpqya.compose.fastscroller.ScrollbarStyle
import io.github.oikvpqya.compose.fastscroller.ThumbStyle
import io.github.oikvpqya.compose.fastscroller.TrackStyle

/**
 * Simple default [ScrollbarStyle] with applying MaterialTheme.
 */
@Composable
fun defaultMaterialScrollbarStyle(): ScrollbarStyle {
    return ScrollbarStyle(
        minimalHeight = 52.dp,
        thickness = 8.dp,
        hoverDurationMillis = 300,
        thumbStyle = ThumbStyle(
            shape = RoundedCornerShape(8.dp),
            unhoverColor = Color(MaterialTheme.colors.primary.toArgb()),
            hoverColor = Color(MaterialTheme.colors.primary.toArgb()),
        ),
        trackStyle = TrackStyle(
            shape = RoundedCornerShape(8.dp),
            unhoverColor = Color((LocalElevationOverlay.current?.apply(MaterialTheme.colors.surface, 8.dp))?.toArgb() ?: 0x00000000),
            hoverColor = Color((LocalElevationOverlay.current?.apply(MaterialTheme.colors.surface, 8.dp))?.toArgb() ?: 0x00000000),
        ),
    )
}
