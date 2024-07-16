package network

import secrets.BuildConfig

object HttpRoutes {
    private const val BASE_URL = "https://api.thingspeak.com"
    const val REQUEST_URL = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID}/feeds.json?"
    const val REQUEST_CHANNEL_FEED = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID}/fields/"
}