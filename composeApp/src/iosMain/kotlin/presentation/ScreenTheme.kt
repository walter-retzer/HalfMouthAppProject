package presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.plcoding.contactscomposemultiplatform.ui.theme.Typography
import theme.darkScheme

@Composable
actual fun ScreenTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) darkScheme else darkScheme,
        typography = Typography,
        content = content
    )
}