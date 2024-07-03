import androidx.compose.ui.window.ComposeUIViewController
import navigation.NavHostMain
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = ComposeUIViewController {
    NavHostMain(
        darkTheme = true,
        dynamicColor = false,
    )
}