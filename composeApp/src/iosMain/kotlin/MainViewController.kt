import androidx.compose.ui.window.ComposeUIViewController
import navigation.NavHostMain
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = ComposeUIViewController {
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle ==
                UIUserInterfaceStyle.UIUserInterfaceStyleDark
    NavHostMain(
        darkTheme = isDarkTheme,
        dynamicColor = false,
    )
}