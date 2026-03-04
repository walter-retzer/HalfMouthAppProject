import androidx.compose.ui.window.ComposeUIViewController
import di.KoinInitializer
import navigation.NavHostMain


fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }
) {
    NavHostMain(
        darkTheme = true,
        dynamicColor = false
    )
}
