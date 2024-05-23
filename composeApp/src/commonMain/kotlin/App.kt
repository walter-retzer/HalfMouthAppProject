import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.ScreenTheme
import screens.SignInScreen


@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
    ScreenTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            //color = Color.White
        ) {
            SignInScreen(Modifier)
        }
    }
}
