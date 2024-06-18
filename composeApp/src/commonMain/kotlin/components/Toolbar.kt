package components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import theme.primaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarLarge(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationLeftIconClick: () -> Unit,
    onNavigationSettingsIconClick: () -> Unit,
    onNavigationProfileIconClick: () -> Unit,
    scrollBehavior:  TopAppBarScrollBehavior? = null,

) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = color,
            scrolledContainerColor = color,
            titleContentColor = titleColor,
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationLeftIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { onNavigationProfileIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = { onNavigationSettingsIconClick() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profileToolbar(
    color: Color = Color.Black,
    titleColor: Color = primaryDark,
    title: String,
    onNavigationIconBack: () -> Unit,
    onNavigationIconClose: () -> Unit,
    scrollBehavior:  TopAppBarScrollBehavior? = null,

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
