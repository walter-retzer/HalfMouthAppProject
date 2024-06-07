package util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun snackBarOnlyMessage(
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            duration = duration
        )
    }
}

fun snackBarWithCloseButton(
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String
) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite
        )
    }
}