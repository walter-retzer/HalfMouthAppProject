import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import database.getTicketDataBase
import di.KoinInitializer
import navigation.NavHostMain


fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }
) {
    val ticketDao = remember { getTicketDataBase().ticketDao() }
    NavHostMain(
        darkTheme = true,
        dynamicColor = false,
        ticketDao = ticketDao
    )
}
