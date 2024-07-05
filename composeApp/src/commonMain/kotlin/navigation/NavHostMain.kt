package navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import components.AppBottomNavigationBar
import database.TicketDao
import navigation.ArgumentsKey.FIELD_ID_KEY
import navigation.ArgumentsKey.FIELD_ID_NAME
import navigation.NavAnimations.popEnterRightAnimation
import navigation.NavAnimations.popExitRightAnimation
import navigation.NavAnimations.slideFadeInAnimation
import navigation.NavAnimations.slideFadeOutAnimation
import navigation.NavAnimations.slideLeftEnterAnimation
import navigation.NavAnimations.slideLeftExitAnimation
import navigation.home.NavItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import presentation.ScreenTheme
import screens.account.LoginScreen
import screens.account.SignInScreen
import screens.charts.ChartLineScreen
import screens.contactInfo.ContactInfoScreen
import screens.discounts.DiscountsScreen
import screens.home.HomeScreen
import screens.production.ProductionScreen
import screens.profile.ProfileScreen
import screens.splash.SplashScreen
import screens.tickets.TicketScreen


@Composable
@Preview
fun NavHostMain(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    ticketDao: TicketDao,
    navController: NavHostController = rememberNavController()
) {
    KoinContext {
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
                    loginNavGraph(navController, ticketDao)
                }
            }
        }
    }
}


private fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    ticketDao: TicketDao,
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
        homeNavGraph(ticketDao)
    }
}

private fun NavGraphBuilder.homeNavGraph(
    ticketDao: TicketDao
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
                        ticketDao = ticketDao,
                        onNavigateFromDrawerMenu = { route->
                            navController.navigate(route)
                        },
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        }
                    )
                }

                composable(
                    route = AppNavigation.ProductionRoute.name,
                ) {
                    ProductionScreen(
                        ticketDao = ticketDao,
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        },
                        onNavigateToChartLine = { fieldId, fieldName ->
                            navController.navigate("${AppNavigation.ChartLineRoute.name}/${fieldId}/${fieldName}") {
                                launchSingleTop = true
                            }
                        },
                        onNavigateFromDrawerMenu = { route->
                            navController.navigate(route)
                        },
                    )
                }

                composable(
                    route = AppNavigation.DiscountsRoute.name,
                ) {
                    DiscountsScreen(
                        ticketDao = ticketDao,
                        onNavigateFromDrawerMenu = { route ->
                            navController.navigate(route)
                        },
                        onNavigateBack = {
                            navController.navigateUp()
                        },
                        viewModel = koinInject(parameters = { parametersOf(ticketDao) })
                    )
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
                        ticketDao = ticketDao,
                        onNavigateToProfile = {
                            navController.navigate(AppNavigation.ProfileRoute.name)
                        },
                        onNavigateFromDrawerMenu = { route->
                            navController.navigate(route)
                        },
                    )
                }

                composable(
                    route = AppNavigation.TicketRoute.name,
                ) {
                    TicketScreen(
                        ticketDao = ticketDao,
                        onNavigateBack = { navController.navigateUp() },
                        onNavigateClose = { navController.navigate(AppNavigation.HomeRoute.name) }
                    )
                }

                composable(
                    route = "${AppNavigation.ChartLineRoute.name}/{$FIELD_ID_KEY}/{$FIELD_ID_NAME}",
                    arguments = listOf(navArgument(FIELD_ID_KEY) { type = NavType.StringType }, navArgument(FIELD_ID_NAME) { type = NavType.StringType }),
                ) {
                    val arguments = requireNotNull(it.arguments)
                    val fieldId = arguments.getString(FIELD_ID_KEY)
                    val fieldName = arguments.getString(FIELD_ID_NAME)

                    ChartLineScreen(
                        ticketDao = ticketDao,
                        fieldId = fieldId.toString(),
                        fieldName = fieldName.toString(),
                        onNavigateToProduction = {
                            navController.navigate(AppNavigation.ProductionRoute.name)
                        },
                        onNavigateFromDrawerMenu = { route->
                            navController.navigate(route)
                        },
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

object ArgumentsKey {
    const val FIELD_ID_KEY = "FIELD_ID"
    const val FIELD_ID_NAME = "FIELD_NAME"
}


@Composable
inline fun <reified T: ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}
