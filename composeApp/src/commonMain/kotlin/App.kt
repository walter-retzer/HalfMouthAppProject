import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.plcoding.contactscomposemultiplatform.ui.theme.DarkColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.SignInScreen


@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = DarkColorScheme
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            SignInScreen(Modifier)
        }
    }
}
