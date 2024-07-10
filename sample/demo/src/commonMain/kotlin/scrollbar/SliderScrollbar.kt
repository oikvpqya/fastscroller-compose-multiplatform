package scrollbar

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import io.github.oikvpqya.compose.fastscroller.maxScrollOffset
import io.github.oikvpqya.compose.fastscroller.rememberScrollbarAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val HandleHeight = 44.0.dp
private val InactiveTrackHeight = 16.0.dp
private val TrackHeight = InactiveTrackHeight
private val ThumbHeight = HandleHeight
private val SliderHeight = max(TrackHeight, ThumbHeight)

@Composable
fun SliderScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val adapter = rememberScrollbarAdapter(state)
    val trackSize = adapter.viewportSize
    val scrollScale = with(adapter) {
        if (maxScrollOffset == 0.0) 1.0 else trackSize / maxScrollOffset
    }
    val currentPosition = scrollScale * adapter.scrollOffset
    val range = 0.0f..trackSize.toFloat()
    val widthPx = with(LocalDensity.current) { SliderHeight.roundToPx() }
    val currentInteractionSource by rememberUpdatedState(interactionSource)
    val dragMutex = remember { Mutex() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier.width(SliderHeight)
    ) {
        Slider(
            value = currentPosition.toFloat(),
            valueRange = range,
            onValueChange = { position ->
                val interaction = DragInteraction.Start()
                currentInteractionSource.tryEmit(interaction)
                coroutineScope.launch {
                    dragMutex.withLock {
                        val maxScrollPosition = adapter.maxScrollOffset * scrollScale
                        val realPosition = position.toDouble().coerceIn(0.0, maxScrollPosition)
                        adapter.scrollTo(realPosition / scrollScale)
                    }
                }
                currentInteractionSource.tryEmit(DragInteraction.Stop(interaction))
            },
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = constraints.minHeight,
                            minHeight = constraints.minWidth,
                            maxWidth = constraints.maxHeight,
                            maxHeight = constraints.maxWidth,
                        )
                    )
                    val x = widthPx + (constraints.maxHeight - constraints.maxWidth).coerceAtLeast(0) / 2
                    layout(
                        width = placeable.width,
                        height = placeable.height,
                    ) {
                        placeable.placeRelative(x, 0)
                    }
                }
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0f)
                    rotationZ = 90f
                },
        )
    }
}
