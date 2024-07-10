import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import scrollbar.DesktopScrollbar
import scrollbar.FastScrollerScrollbar
import scrollbar.SliderScrollbar

enum class ScrollbarSelection {
    Desktop, FastScroller, Slider
}

@Composable
fun Screen() {
    val state = rememberLazyListState()
    var selected by remember { mutableStateOf(ScrollbarSelection.Desktop) }

    Column {
        SegmentedButtons(selected) { selected = it }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
                items(20) { index -> TextItem(index = index) }
            }
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                when(selected) {
                    ScrollbarSelection.Desktop -> {
                        DesktopScrollbar(
                            state = state,
                            modifier = Modifier
                                .fillMaxHeight(),
                        )
                    }
                    ScrollbarSelection.FastScroller -> {
                        FastScrollerScrollbar(
                            state = state,
                            modifier = Modifier
                                .fillMaxHeight(),
                        )
                    }
                    ScrollbarSelection.Slider -> {
                        SliderScrollbar(
                            state = state,
                            modifier = Modifier
                                .fillMaxHeight(),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedButtons(
    selected: ScrollbarSelection,
    onClick: (ScrollbarSelection) -> Unit,
) {
    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
            onClick = { onClick(ScrollbarSelection.Desktop) },
            selected = selected == ScrollbarSelection.Desktop,
        ) {
            Text("Desktop")
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
            onClick = { onClick(ScrollbarSelection.FastScroller) },
            selected = selected == ScrollbarSelection.FastScroller,
        ) {
            Text("FastScroller")
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
            onClick = { onClick(ScrollbarSelection.Slider) },
            selected = selected == ScrollbarSelection.Slider,
        ) {
            Text("Slider")
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
