package navigation

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.screen_login_name
import halfmouthappproject.composeapp.generated.resources.sign_in_name
import halfmouthappproject.composeapp.generated.resources.splash_screen_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
enum class AppNavigation(val title: StringResource) {
    SplashScreenRoute(title = Res.string.splash_screen_name),
    LoginRoute(title = Res.string.screen_login_name),
    SignInRoute(title = Res.string.sign_in_name),
}