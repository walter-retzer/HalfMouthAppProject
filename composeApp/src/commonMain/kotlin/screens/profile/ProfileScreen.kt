package screens.profile

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import components.AppToolbarLarge
import components.BottomNavigationBar
import data.UserPreferences
import kotlinx.coroutines.launch
import util.snackBarOnlyMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProfile() {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val settingsPref = Settings()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            AppToolbarLarge(
                title = "Perfil",
                onNavigationLeftIconClick = {
                    scope.launch {
                        val test: String? = settingsPref[UserPreferences.UID]
                        val name: String? = settingsPref[UserPreferences.NAME]
                        val email: String? = settingsPref[UserPreferences.EMAIL]
                        val phone: String? = settingsPref[UserPreferences.PHONE]
                        println(test)
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = "$test $email $name $phone"
                        )
                    }

                },
                onNavigationProfileIconClick = { },
                onNavigationSettingsIconClick = { },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = Color.Black,
                containerColor = Color.Black
            )
            {
                BottomNavigationBar(navController = rememberNavController())
            }
        }
    ) {


    }
}