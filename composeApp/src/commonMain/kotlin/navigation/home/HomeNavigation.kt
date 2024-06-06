package navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavPath {
    HOME, SEARCH, LIST, PROFILE
}

object NavTitle {
    const val HOME = "Home"
    const val SEARCH = "Search"
    const val LIST = "List"
    const val PROFILE = "Profile"
}

open class Item(val path: String, val title: String, val icon: ImageVector)
sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object Search :
        Item(path = NavPath.SEARCH.toString(), title = NavTitle.SEARCH, icon = Icons.Default.Search)

    object List :
        Item(path = NavPath.LIST.toString(), title = NavTitle.LIST, icon = Icons.Default.List)

    object Profile :
        Item(
            path = NavPath.PROFILE.toString(), title = NavTitle.PROFILE, icon = Icons.Default.Person
        )
}