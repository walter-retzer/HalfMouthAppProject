package presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import theme.AppTypography
import theme.darkScheme


@Composable
actual fun ScreenTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) darkScheme else darkScheme,
        typography = AppTypography(),
        content = content
    )
}
