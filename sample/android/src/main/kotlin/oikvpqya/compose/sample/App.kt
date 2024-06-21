package oikvpqya.compose.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun App() {
    AppTheme {
        Surface {
            Screen()
        }
    }
}

@Composable
private fun AppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
    MaterialTheme(
        colors = colors,
        content = content,
    )
}
