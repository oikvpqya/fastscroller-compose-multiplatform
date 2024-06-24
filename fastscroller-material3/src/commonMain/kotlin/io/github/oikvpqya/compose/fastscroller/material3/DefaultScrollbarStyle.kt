package io.github.oikvpqya.compose.fastscroller.material3

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
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
            shape = RoundedCornerShape(4.dp),
            unhoverColor = Color(MaterialTheme.colorScheme.primary.toArgb()),
            hoverColor = Color(MaterialTheme.colorScheme.primary.toArgb()),
        ),
        trackStyle = TrackStyle(
            shape = RoundedCornerShape(4.dp),
            unhoverColor = Color(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp).toArgb()),
            hoverColor = Color(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp).toArgb()),
        ),
    )
}
