import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import navigation.RootComponentNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.LoginScreen
import screens.SignInScreen
import screens.SplashScreen
import theme.darkScheme
import theme.typography


@Composable
@Preview
fun App(rootComponent: RootComponentNavigation) {
    MaterialTheme(
        colorScheme = darkScheme,
        typography = typography,
    ) {
        val childStack by rootComponent.childStack.subscribeAsState()

        Surface(modifier = Modifier.fillMaxSize()) {}

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when(val instance = child.instance){
                is RootComponentNavigation.Child.ScreenSplashScreen -> SplashScreen(instance.component)
                is RootComponentNavigation.Child.ScreenLoginChild -> LoginScreen(component= instance.component)
                is RootComponentNavigation.Child.ScreenSignIn -> SignInScreen()
            }
        }
    }
}
