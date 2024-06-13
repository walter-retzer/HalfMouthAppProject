package screens.production

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import viewmodel.ProductionViewModel

@Composable
fun ProductionScreen() {

    val viewModel = getViewModel(
        key = "production-screen",
        factory = viewModelFactory {
            ProductionViewModel()
        }
    )
    val uiState by viewModel.uiState.collectAsState()

    viewModel.loadFirstShimmer()

    Text(
        modifier = Modifier,
        text = uiState.feeds.toString(),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary,
    )

}