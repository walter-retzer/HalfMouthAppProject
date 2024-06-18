package util

import androidx.compose.runtime.Composable
import platform.posix.exit

@Composable
actual fun RestartApp() {
    exit(0)
}
