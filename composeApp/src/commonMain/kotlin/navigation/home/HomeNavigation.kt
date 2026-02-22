package navigation.home

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_beer
import halfmouthappproject.composeapp.generated.resources.icon_home
import halfmouthappproject.composeapp.generated.resources.icon_input_value
import halfmouthappproject.composeapp.generated.resources.icon_phone
import navigation.AppNavigation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi


object NavTitle {
    const val HOME = "Menu"
    const val PRODUCTION = "Produção"
    const val SETPPOINT = "Setpoint"
    const val CONTACT = "Contato"
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

    object Setpoint :
        Item(
            pathRoute = AppNavigation.SetpointRoute.name,
            title = NavTitle.SETPPOINT,
            icon = Res.drawable.icon_input_value
        )

    object Contact :
        Item(
            pathRoute = AppNavigation.ContactRoute.name,
            title = NavTitle.CONTACT,
            icon = Res.drawable.icon_phone
        )
}
