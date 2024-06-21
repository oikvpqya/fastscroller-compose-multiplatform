package oikvpqya.compose.sample

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oikvpqya.compose.fastscroller.VerticalScrollbar
import oikvpqya.compose.fastscroller.material.defaultMaterialScrollbarStyle
import oikvpqya.compose.fastscroller.rememberScrollbarAdapter

@Composable
fun Screen() {
    val state = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
        ) { items(100) { index -> TextItem(index = index) } }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = state),
            style = defaultMaterialScrollbarStyle().copy(
                minimalHeight = 32.dp,
                thickness = 32.dp,
            ),
            enablePressToScroll = false,
        )
    }
}

@Composable
fun TextItem(index: Int) {
    Text(
        text = "Item $index",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(width = 1.dp, MaterialTheme.colors.primary)
            .padding(16.dp)
    )
}
