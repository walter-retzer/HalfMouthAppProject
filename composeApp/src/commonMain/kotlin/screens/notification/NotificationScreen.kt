package screens.notification

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import components.profileToolbar
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val viewModel = getViewModel(
        key = "notification-screen",
        factory = viewModelFactory {
            NotificationViewModel()
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            profileToolbar(
                title = "Notification",
                onNavigationIconBack = {  },
                onNavigationIconClose = {  },
                scrollBehavior = scrollBehavior
            )
        }
    ) {

        Text(
            modifier = Modifier.padding(it),
            text = viewModel.response.toString()
        )
    }
}