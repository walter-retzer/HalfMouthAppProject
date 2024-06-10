package navigation.home

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_account
import halfmouthappproject.composeapp.generated.resources.icon_beer
import halfmouthappproject.composeapp.generated.resources.icon_home
import halfmouthappproject.composeapp.generated.resources.icon_notifications
import navigation.AppNavigation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi


object NavTitle {
    const val HOME = "Menu"
    const val PRODUCTION = "Produção"
    const val NOTIFICATIONS = "Notificação"
    const val PROFILE = "Perfil"
}

@OptIn(ExperimentalResourceApi::class)
open class Item(
    val pathRoute: String,
    val title: String,
    val icon: DrawableResource,
)

@OptIn(ExperimentalResourceApi::class)
sealed class NavItem {
    object Home :
        Item(
            pathRoute = AppNavigation.HomeRoute.name,
            title = NavTitle.HOME,
            icon = Res.drawable.icon_home
        )

    object Production :
        Item(
            pathRoute = AppNavigation.ProductionRoute.name,
            title = NavTitle.PRODUCTION,
            icon = Res.drawable.icon_beer
        )

    object Notification :
        Item(
            pathRoute = AppNavigation.NotificationRoute.name,
            title = NavTitle.NOTIFICATIONS,
            icon = Res.drawable.icon_notifications
        )

    object Profile :
        Item(
            pathRoute = AppNavigation.ProfileRoute.name,
            title = NavTitle.PROFILE,
            icon = Res.drawable.icon_account
        )
}
