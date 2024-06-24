package io.github.oikvpqya.compose.fastscroller.indicator

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.sqrt

class IndicatorShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                val height = size.height
                val r = height / 2
                val sqrt2 = sqrt(2.0).toFloat()
                val width = max(r + sqrt2 * r, size.width)
                arcTo(r, r, r, 90f, 180f)
                val o1X = width - sqrt2 * r
                arcTo(o1X, r, r, -90f, 45f)
                val r2 = r / 5
                val o2X = width - sqrt2 * r2
                arcTo(o2X, r, r2, -45f, 90f)
                arcTo(o1X, r, r, 45f, 45f)
                moveTo(r, r)
                close()
            }
        )
    }
}

private fun Path.arcTo(
    centerX: Float,
    centerY: Float,
    radius: Float,
    startAngle: Float,
    sweepAngle: Float,
) {
    arcTo(
        rect = Rect(
            left = centerX - radius,
            top = centerY - radius,
            right = centerX + radius,
            bottom = centerY + radius,
        ),
        startAngleDegrees = startAngle,
        sweepAngleDegrees = sweepAngle,
        forceMoveTo = false,
    )
}
