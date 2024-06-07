package navigation.home

import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_beer
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

enum class NavPath {
    HOME, SEARCH, LIST, PROFILE
}

object NavTitle {
    const val HOME = "Home"
    const val SEARCH = "Search"
    const val LIST = "List"
    const val PROFILE = "Profile"
}

@OptIn(ExperimentalResourceApi::class)
open class Item(val path: String, val title: String, val icon: DrawableResource)
@OptIn(ExperimentalResourceApi::class)
sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Res.drawable.icon_beer)

    object Search :
        Item(path = NavPath.SEARCH.toString(), title = NavTitle.SEARCH, icon = Res.drawable.icon_beer )

    object List :
        Item(path = NavPath.LIST.toString(), title = NavTitle.LIST, icon = Res.drawable.icon_beer)

    object Profile :
        Item(
            path = NavPath.PROFILE.toString(), title = NavTitle.PROFILE, icon = Res.drawable.icon_beer
        )
}
