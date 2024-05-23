package presentation
import androidx.compose.runtime.Composable

@Composable
expect fun ScreenTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)