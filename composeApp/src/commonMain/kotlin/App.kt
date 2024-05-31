import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.serialization.json.JsonNull.content
import navigation.RootComponentNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.ScreenTheme
import screens.LoginScreen
import screens.SignInScreen
import screens.SplashScreen
import theme.typography


@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    rootComponent: RootComponentNavigation
) {

//    ScreenTheme(
//        darkTheme = darkTheme,
//        dynamicColor = dynamicColor
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            //color = Color.White
//        ) {
//            SplashScreen(Modifier)
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
    ) {
        val childStack by rootComponent.childStack.subscribeAsState()
        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when(val instance = child.instance){
                is RootComponentNavigation.Child.ScreenSplashScreen -> SplashScreen(instance.component)
                is RootComponentNavigation.Child.ScreenLoginChild -> LoginScreen()
                is RootComponentNavigation.Child.ScreenSignIn -> LoginScreen()
            }
        }
    }
}
