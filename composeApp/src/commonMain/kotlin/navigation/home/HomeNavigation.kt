package navigation.home

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_account
import halfmouthappproject.composeapp.generated.resources.icon_beer
import halfmouthappproject.composeapp.generated.resources.icon_home
import halfmouthappproject.composeapp.generated.resources.icon_notifications
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

enum class NavPath {
    HOME, PRODUCTION, NOTIFICATION, PROFILE
}

object NavTitle {
    const val HOME = "Menu"
    const val PRODUCTION = "Produção"
    const val NOTIFICATIONS = "Notificação"
    const val PROFILE = "Perfil"
}

@OptIn(ExperimentalResourceApi::class)
open class Item(val path: String, val title: String, val icon: DrawableResource)
@OptIn(ExperimentalResourceApi::class)
sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Res.drawable.icon_home)

    object Production :
        Item(path = NavPath.PRODUCTION.toString(), title = NavTitle.PRODUCTION, icon = Res.drawable.icon_beer )

    object Notification :
        Item(path = NavPath.NOTIFICATION.toString(), title = NavTitle.NOTIFICATIONS, icon = Res.drawable.icon_notifications)

    object Profile :
        Item(
            path = NavPath.PROFILE.toString(), title = NavTitle.PROFILE, icon = Res.drawable.icon_account
        )
}
