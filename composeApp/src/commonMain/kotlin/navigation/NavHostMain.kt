package navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import navigation.NavAnimations.popEnterRightAnimation
import navigation.NavAnimations.popExitRightAnimation
import navigation.NavAnimations.slideLeftEnterAnimation
import navigation.NavAnimations.slideLeftExitAnimation
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.ScreenTheme
import screens.account.LoginScreen
import screens.account.SignInScreen
import screens.home.HomeScreen
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
                homeNavGraph(navController)
                notificationNavGraph(navController)
                profileNavGraph(navController)
            }
        }
    }
}


private fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppGraphNav.LoginGraph.name,
        startDestination = AppNavigation.HomeRoute.name
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
                    navController.navigate(AppGraphNav.HomeGraph.name){
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
        composable(route = AppNavigation.HomeRoute.name) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(AppNavigation.ProfileRoute.name)
                },
                navController = navController
            )
        }
    }
}


private fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppGraphNav.HomeGraph.name,
        startDestination = AppNavigation.ProfileRoute.name
    ) {

        composable(route = AppNavigation.ProfileRoute.name) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(AppNavigation.ProfileRoute.name)
                },
                navController = navController
            )
        }
    }
}
private fun NavGraphBuilder.notificationNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppGraphNav.ProfileGraph.name,
        startDestination = AppNavigation.ProfileRoute.name
    ) {

        composable(route = AppNavigation.ProfileRoute.name) {
            ProfileScreen()
        }
    }
}

private fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppGraphNav.NotificationGraph.name,
        startDestination = AppNavigation.ProfileRoute.name
    ) {

        composable(route = AppNavigation.ProfileRoute.name) {
            ProfileScreen()
        }
    }
}