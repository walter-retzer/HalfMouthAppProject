package components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import navigation.home.NavItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onNavigateToProfile: () -> Unit
) {

    val navItems = listOf(NavItem.Home, NavItem.Production, NavItem.Notification, NavItem.Profile)
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.Black
    ) {
        navItems.forEachIndexed { index, item ->
            println(navController.graph.startDestinationRoute)
            println(item.pathRoute)
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(vectorResource( item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = navController.graph.startDestinationRoute == item.pathRoute,
                onClick = {
                            navController.navigate(item.pathRoute)

//                    navController.navigate(item.path) {
//                        navController.graph.startDestinationRoute?.let { route ->
//                            popUpTo(route) { saveState = true }
//                        }
//                    launchSingleTop = true
//                    restoreState = true
//                    }
                }
            )
        }
    }
}
