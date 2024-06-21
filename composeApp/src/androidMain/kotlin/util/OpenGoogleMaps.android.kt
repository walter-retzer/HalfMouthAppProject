package util

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun OpenGoogleMaps() {
    val mapsUri = Uri.parse("google.navigation:q=-23.46511,-47.47381&mode=d&mode=d")
    LocalContext.current.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            mapsUri
        ).setPackage("com.google.android.apps.maps")
    )
}
