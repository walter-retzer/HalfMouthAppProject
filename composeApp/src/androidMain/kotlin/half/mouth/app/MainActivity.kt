package half.mouth.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import navigation.NavHostMain


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = actionBar
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor= Color.BLACK
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            NavHostMain(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false,
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    NavHostMain(
        darkTheme = isSystemInDarkTheme(),
        dynamicColor = true,
    )
}