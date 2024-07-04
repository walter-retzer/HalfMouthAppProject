package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_notifications
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import theme.primaryDark

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun MenuToolbar(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationToMenu: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigationToProfile: () -> Unit,
    scrollBehavior:  TopAppBarScrollBehavior? = null,
    isActivatedBadge: Boolean = false
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = color,
            scrolledContainerColor = color,
            titleContentColor = titleColor,
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationToMenu() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            BadgedBox(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable { onNavigateToNotifications() },
                badge = {
                    if (isActivatedBadge) {
                        Badge(
                            modifier = Modifier.padding(start = 3.dp, bottom = 10.dp)
                        ) {
                            Text(text = "1")
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.icon_notifications),
                    contentDescription = null
                )
            }
            IconButton(onClick = { onNavigationToProfile() }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleToolbar(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationToMenu: () -> Unit,
    onNavigationClose: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = color,
            scrolledContainerColor = color,
            titleContentColor = titleColor,
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationToMenu() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { onNavigationClose() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationIconBack: () -> Unit,
    onNavigationIconClose: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = color,
            scrolledContainerColor = color,
            titleContentColor = titleColor,
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { onNavigationIconClose() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsToolbar(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationIconBack: () -> Unit,
    onNavigationIconClose: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = color,
            scrolledContainerColor = color,
            titleContentColor = titleColor,
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { onNavigationIconClose() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}
