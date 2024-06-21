import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import oikvpqya.compose.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scrollbar",
        state = rememberWindowState(width = 250.dp, height = 400.dp),
    ) {
        App()
    }
}
