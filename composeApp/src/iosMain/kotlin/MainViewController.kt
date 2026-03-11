import androidx.compose.ui.window.ComposeUIViewController
import di.KoinInitializer
import navigation.NavHostMain
import platform.UIKit.UIViewController
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController(): UIViewController {

    val controller = ComposeUIViewController(
        configure = {
            KoinInitializer().init()
        }
    ) {
        NavHostMain(
            darkTheme = true,
            dynamicColor = false
        )
    }

    // força modo dark no iOS
    controller.overrideUserInterfaceStyle = UIUserInterfaceStyle.UIUserInterfaceStyleDark

    // evita fundo branco nas safe areas
    controller.view.backgroundColor = UIColor.blackColor

    return controller
}
