package navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import components.AppBottomNavigationBar
import navigation.NavAnimations.popEnterRightAnimation
import navigation.NavAnimations.popExitRightAnimation
import navigation.NavAnimations.slideFadeInAnimation
import navigation.NavAnimations.slideFadeOutAnimation
import navigation.NavAnimations.slideLeftEnterAnimation
import navigation.NavAnimations.slideLeftExitAnimation
import navigation.home.NavItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.ScreenTheme
import screens.account.LoginScreen
import screens.account.SignInScreen
import screens.contactInfo.ContactInfoScreen
import screens.home.HomeScreen
import screens.discounts.DiscountsScreen
import screens.production.ProductionScreen
import screens.profile.ProfileScreen
import screens.splash.SplashScreen


@Composable
@Preview
fun NavHostMain(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    navController: NavHostController = rememberNavController()
) {
    ScreenTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = AppGraphNav.LoginGraph.name,
                enterTransition = slideLeftEnterAnimation,
                exitTransition = slideLeftExitAnimation,
                popEnterTransition = popEnterRightAnimation,
                popExitTransition = popExitRightAnimation
            ) {
                loginNavGraph(navController)
            }
        }
    }
}


private fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppGraphNav.LoginGraph.name,
        startDestination = AppNavigation.SplashScreenRoute.name
    ) {
        composable(route = AppNavigation.SplashScreenRoute.name) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(AppNavigation.LoginRoute.name) {
                        popUpTo(AppNavigation.SplashScreenRoute.name) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppGraphNav.HomeGraph.name) {
                        popUpTo(AppNavigation.SplashScreenRoute.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = AppNavigation.LoginRoute.name) {
            LoginScreen(
                onNavigateToSignIn = {
                    navController.navigate(AppNavigation.SignInRoute.name)
                },
                onNavigateToHome = {
                    navController.navigate(AppGraphNav.HomeGraph.name) {
                        popUpTo(AppNavigation.LoginRoute.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = AppNavigation.SignInRoute.name) {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(AppGraphNav.HomeGraph.name)
                }
            )
        }
        homeNavGraph()
    }
}

private fun NavGraphBuilder.homeNavGraph(
) {
    composable(
        route = AppGraphNav.HomeGraph.name,
        enterTransition = slideFadeInAnimation,
        exitTransition = slideFadeOutAnimation,
        popEnterTransition = slideFadeInAnimation,
        popExitTransition = slideFadeOutAnimation
    ) {

        val items = remember {
            listOf(
                NavItem.Home,
                NavItem.Production,
                NavItem.Discounts,
                NavItem.Contact
            )
        }
        val navController = rememberNavController()

        Column(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = AppNavigation.HomeRoute.name,
                Modifier.weight(1f)
            ) {
                composable(route = AppNavigation.HomeRoute.name) {
                    HomeScreen(
                        onNavigateToDrawerMenu = { },
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        }
                    )
                }

                composable(
                    route = AppNavigation.ProductionRoute.name,
                ) {
                    ProductionScreen(
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        }
                    )
                }

                composable(
                    route = AppNavigation.DiscountsRoute.name,
                ) {
                    DiscountsScreen()
                }

                composable(
                    route = AppNavigation.ProfileRoute.name,
                ) {
                    ProfileScreen(
                        onNavigateBack = {
                            navController.navigateUp()
                        }
                    )
                }

                composable(
                    route = AppNavigation.ContactRoute.name,
                ) {
                    ContactInfoScreen(
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        }
                    )
                }
            }

            AppBottomNavigationBar(
                navItems = items,
                navController = navController
            )
        }
    }
}
