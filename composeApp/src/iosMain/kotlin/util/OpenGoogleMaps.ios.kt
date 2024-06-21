package util

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun OpenGoogleMaps() {
    val mapsUrl = "http://maps.google.com/?q=-23.46511,-47.47381&mode=d"
    val nsUrl = mapsUrl.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}