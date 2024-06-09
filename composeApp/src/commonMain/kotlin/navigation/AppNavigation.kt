package navigation

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.home_graph_name
import halfmouthappproject.composeapp.generated.resources.home_name
import halfmouthappproject.composeapp.generated.resources.login_graph_name
import halfmouthappproject.composeapp.generated.resources.notification_graph_name
import halfmouthappproject.composeapp.generated.resources.production_graph_name
import halfmouthappproject.composeapp.generated.resources.profile_graph_name
import halfmouthappproject.composeapp.generated.resources.profile_name
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
    HomeRoute(title = Res.string.home_name),
    ProfileRoute(title = Res.string.profile_name),
}

@OptIn(ExperimentalResourceApi::class)
enum class AppGraphNav(val title: StringResource) {
    LoginGraph(title = Res.string.login_graph_name),
    HomeGraph(title = Res.string.home_graph_name),
    ProfileGraph(title = Res.string.profile_graph_name),
    ProductionGraph(title = Res.string.production_graph_name),
    NotificationGraph(title = Res.string.notification_graph_name),
}