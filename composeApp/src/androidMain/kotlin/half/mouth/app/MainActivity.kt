package half.mouth.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import database.getTicketDataBase
import navigation.NavHostMain


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = actionBar
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor= Color.BLACK
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        val ticketDao = getTicketDataBase(applicationContext).ticketDao()

        setContent {
            NavHostMain(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false,
                ticketDao = ticketDao
            )
        }
    }
}
