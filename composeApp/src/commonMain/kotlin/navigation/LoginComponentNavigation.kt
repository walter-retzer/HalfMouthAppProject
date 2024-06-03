package navigation

import com.arkivanov.decompose.ComponentContext

class LoginComponentNavigation(
    componentContext: ComponentContext,
    private val onNavigateToSignIn: () -> Unit,
): ComponentContext by componentContext {

    fun onEvent(event: LoginNavigationEvent){
        when(event){
            LoginNavigationEvent.NavigateToMainMenu -> TODO()
            LoginNavigationEvent.NavigateToSignIn -> { onNavigateToSignIn()}
        }
    }

}
sealed interface LoginNavigationEvent{
    data object NavigateToMainMenu: LoginNavigationEvent
    data object NavigateToSignIn: LoginNavigationEvent
}