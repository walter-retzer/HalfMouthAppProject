package navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.ScreenTheme
import screens.LoginScreen
import screens.SignInScreen
import screens.SplashScreen


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
                startDestination = AppNavigation.SplashScreenRoute.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                }
            ) {
                composable(route = AppNavigation.SplashScreenRoute.name) {
                    SplashScreen(
                        onNavigateToLogin = {
                            navController.navigate(AppNavigation.LoginRoute.name) {
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
                        }
                    )
                }
                composable(route = AppNavigation.SignInRoute.name) {
                    SignInScreen()
                }
            }
        }
    }
}
