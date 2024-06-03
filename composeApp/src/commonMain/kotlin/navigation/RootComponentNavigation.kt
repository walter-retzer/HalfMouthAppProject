package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable

class RootComponentNavigation(
    componentContext: ComponentContext
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.ScreenSplashScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.ScreenSplashScreen -> Child.ScreenSplashScreen(
                SplashScreenComponentNavigation(
                    componentContext = context,
                    //onNavigateToSignIn = { navigation.push(Configuration.ScreenSignIn) },
                    onNavigateToLogin = { navigation.pushNew(Configuration.ScreenLogin)}
                )
            )

            is Configuration.ScreenLogin -> Child.ScreenLoginChild(
                LoginComponentNavigation(
                    componentContext = context,
                    onNavigateToSignIn = { navigation.pushNew(Configuration.ScreenSignIn) }
                )
            )

            is Configuration.ScreenSignIn -> Child.ScreenSignIn(
                SignInComponentNavigation(context)
            )
        }
    }

    sealed class Child {
        data class ScreenSplashScreen(val component: SplashScreenComponentNavigation): Child()
        data class ScreenLoginChild(val component: LoginComponentNavigation): Child()
        data class ScreenSignIn(val component: SignInComponentNavigation): Child()
    }

    @Serializable
    sealed class Configuration{
        @Serializable
        data object ScreenSplashScreen: Configuration()
        @Serializable
        data object ScreenLogin: Configuration()
        @Serializable
        data object ScreenSignIn: Configuration()
    }
}