package util

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun OpenWhatsAppChat() {

    val toNumber = "+5515991080703"
    val msg = "Olá!"

    LocalContext.current.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                String.format(
                    "https://api.whatsapp.com/send?phone=%s&text=%s",
                    toNumber,
                    msg
                )
            )
        )
    )
}
