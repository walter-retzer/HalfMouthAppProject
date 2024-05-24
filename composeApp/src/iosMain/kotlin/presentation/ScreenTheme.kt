package presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import theme.typography
import theme.darkScheme

@Composable
actual fun ScreenTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) darkScheme else darkScheme,
        typography = typography,
        content = content
    )
}