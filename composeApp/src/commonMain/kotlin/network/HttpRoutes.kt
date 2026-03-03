package network

import secrets.BuildConfig

object HttpRoutes {
    private const val BASE_URL = "https://api.thingspeak.com"
    const val REQUEST_URL_READ_SETPOINT = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID_READ_SETPOINT}/feeds.json?"
    const val REQUEST_URL_READ_TEMPERATURE = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID_READ_TEMPERATURE}/feeds.json?"
    const val REQUEST_URL_READ_STATUS = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID_READ_STATUS}/feeds.json?"
    const val REQUEST_CHANNEL_FEED = "$BASE_URL/channels/${BuildConfig.CHANNEL_ID_READ_SETPOINT}/fields/"
    const val REQUEST_URL_UPDATE_FIELDS = "$BASE_URL/update"
}