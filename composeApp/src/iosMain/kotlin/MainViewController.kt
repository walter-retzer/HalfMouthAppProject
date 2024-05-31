import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import navigation.RootComponentNavigation


fun MainViewController() = ComposeUIViewController {
    val root = remember{ RootComponentNavigation(DefaultComponentContext(LifecycleRegistry()))}
    App(rootComponent = root)
}