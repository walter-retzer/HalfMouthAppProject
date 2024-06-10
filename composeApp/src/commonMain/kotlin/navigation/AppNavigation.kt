package navigation

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.home_graph_name
import halfmouthappproject.composeapp.generated.resources.home_route
import halfmouthappproject.composeapp.generated.resources.login_graph_name
import halfmouthappproject.composeapp.generated.resources.login_route
import halfmouthappproject.composeapp.generated.resources.notification_route
import halfmouthappproject.composeapp.generated.resources.production_route
import halfmouthappproject.composeapp.generated.resources.profile_route
import halfmouthappproject.composeapp.generated.resources.sign_in_route
import halfmouthappproject.composeapp.generated.resources.splash_screen_route
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
enum class AppNavigation(val title: StringResource) {
    SplashScreenRoute(title = Res.string.splash_screen_route),
    LoginRoute(title = Res.string.login_route),
    SignInRoute(title = Res.string.sign_in_route),
    HomeRoute(title = Res.string.home_route),
    ProfileRoute(title = Res.string.profile_route),
    ProductionRoute(title = Res.string.production_route),
    NotificationRoute(title = Res.string.notification_route),
}

@OptIn(ExperimentalResourceApi::class)
enum class AppGraphNav(val title: StringResource) {
    LoginGraph(title = Res.string.login_graph_name),
    HomeGraph(title = Res.string.home_graph_name),
}