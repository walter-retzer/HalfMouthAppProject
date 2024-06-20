package util

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun OpenWhatsAppChat() {

    val urlWhats = "https://api.whatsapp.com/send?phone=+5515991080703&text=Ola"

    val nsUrl = urlWhats.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)

}
