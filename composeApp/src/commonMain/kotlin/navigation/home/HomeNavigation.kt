package navigation.home

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_beer
import halfmouthappproject.composeapp.generated.resources.icon_discount
import halfmouthappproject.composeapp.generated.resources.icon_home
import halfmouthappproject.composeapp.generated.resources.icon_phone
import navigation.AppNavigation
import org.jetbrains.compose.resources.DrawableResource


object NavTitle {
    const val HOME = "Menu"
    const val PRODUCTION = "Produção"
    const val DISCOUNTS = "Descontos"
    const val CONTACT = "Contato"
}

open class Item(
    val pathRoute: String,
    val title: String,
    val icon: DrawableResource,
)

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

    object Discounts :
        Item(
            pathRoute = AppNavigation.DiscountsRoute.name,
            title = NavTitle.DISCOUNTS,
            icon = Res.drawable.icon_discount
        )

    object Contact :
        Item(
            pathRoute = AppNavigation.ContactRoute.name,
            title = NavTitle.CONTACT,
            icon = Res.drawable.icon_phone
        )
}
