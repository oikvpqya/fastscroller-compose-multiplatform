package scrollbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import io.github.oikvpqya.compose.fastscroller.VerticalScrollbar
import io.github.oikvpqya.compose.fastscroller.indicator.IndicatorConstants
import io.github.oikvpqya.compose.fastscroller.material3.defaultMaterialScrollbarStyle
import io.github.oikvpqya.compose.fastscroller.rememberScrollbarAdapter

@Composable
fun FastScrollerScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val adapter = rememberScrollbarAdapter(state)
    val isDragging by interactionSource.collectIsDraggedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val indicatorAlpha by animateFloatAsState(if (isDragging || isHovered) 1f else 0f)
    Box(
        modifier = modifier,
    ) {
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.TopEnd),
            adapter = adapter,
            interactionSource = interactionSource,
            style = defaultMaterialScrollbarStyle(),
            enablePressToScroll = false,
            indicator = { position, isVisible ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = IndicatorConstants.Default.PADDING)
                        .graphicsLayer {
                            val y = -((IndicatorConstants.Default.MIN_HEIGHT / 2).toPx())
                            translationY = (y + position).coerceAtLeast(0f)
                            alpha = indicatorAlpha
                        },
                ) {
                    val indicatorColor = if (isVisible) MaterialTheme.colorScheme.primary else Color.Transparent
                    val textColor = if (isVisible) MaterialTheme.colorScheme.onPrimary else Color.Transparent
                    Box(
                        modifier = Modifier
                            .defaultMinSize(
                                minHeight = IndicatorConstants.Default.MIN_HEIGHT,
                                minWidth = IndicatorConstants.Default.MIN_WIDTH,
                            )
                            .graphicsLayer {
                                clip = true
                                shape = IndicatorConstants.Default.SHAPE
                            }
                            .drawBehind { drawRect(indicatorColor) },
                    )
                    Text(
                        text = "${state.firstVisibleItemIndex}",
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .wrapContentHeight()
                            .padding(end = IndicatorConstants.Default.PADDING)
                            .width(IndicatorConstants.Default.MIN_HEIGHT),
                    )
                }
            }
        )
    }
}

