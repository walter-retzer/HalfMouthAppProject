package navigation

import com.arkivanov.decompose.ComponentContext

class SplashScreenComponentNavigation(
    componentContext: ComponentContext,
    private val onNavigateToLogin: () -> Unit,
): ComponentContext by componentContext {

    fun onEvent(event: SplashScreenEvent){
        when(event){
            is SplashScreenEvent.NavigationToLogin -> { onNavigateToLogin()}
            is SplashScreenEvent.NavigationToSignIn -> {  }
        }
    }
}

sealed interface SplashScreenEvent{
    data object NavigationToLogin: SplashScreenEvent
    data object NavigationToSignIn: SplashScreenEvent
}