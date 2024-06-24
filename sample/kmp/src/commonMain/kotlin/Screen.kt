import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import io.github.oikvpqya.compose.fastscroller.VerticalScrollbar
import io.github.oikvpqya.compose.fastscroller.indicator.IndicatorConstants
import io.github.oikvpqya.compose.fastscroller.material3.defaultMaterialScrollbarStyle
import io.github.oikvpqya.compose.fastscroller.rememberScrollbarAdapter

@Composable
fun Screen() {
    val state = rememberLazyListState()
    var enablePressToScroll by remember { mutableStateOf(false) }
    var isScrollInProgress by remember { mutableStateOf(false) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrollInProgress = it }
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val indicatorAlpha by animateFloatAsState(if (isDragging || isHovered) 1f else 0f)
    val scrollbarAlpha = remember { Animatable(0f) }
    LaunchedEffect(isDragging, isHovered, isScrollInProgress) {
        launch {
            if (isDragging || isHovered || isScrollInProgress) {
                scrollbarAlpha.animateTo(1f)
            } else {
                delay(1000L)
                scrollbarAlpha.animateTo(0f)
            }
        }
    }

    @Composable
    fun TextItem(index: Int) {
        Text(
            text = "Item $index",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(width = 1.dp, MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        )
    }

    @Composable
    fun Checkbox(text: String) {
        val checkedState = remember { mutableStateOf(enablePressToScroll) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { value ->
                    checkedState.value = value
                    enablePressToScroll = value
                },
            )
            Text(text = text)
        }
    }

    Column {
        Checkbox(text = "Enable PressToScroll Scrollbar")
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
                items(20) { index -> TextItem(index = index) }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .graphicsLayer {
                        alpha = scrollbarAlpha.value
                    },
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                ),
                interactionSource = interactionSource,
                style = defaultMaterialScrollbarStyle(),
                enablePressToScroll = enablePressToScroll,
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
}
