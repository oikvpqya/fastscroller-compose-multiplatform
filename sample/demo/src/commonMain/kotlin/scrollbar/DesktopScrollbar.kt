package scrollbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DesktopScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier,
)
