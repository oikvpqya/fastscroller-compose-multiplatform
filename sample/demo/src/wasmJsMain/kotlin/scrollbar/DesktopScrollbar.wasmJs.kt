package scrollbar

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun DesktopScrollbar(
    state: LazyListState,
    modifier: Modifier,
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(state)
    )
}
