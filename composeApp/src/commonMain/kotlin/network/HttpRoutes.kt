package network

object HttpRoutes {
    private const val BASE_URL = "https://api.thingspeak.com"
    const val REQUEST_URL = "$BASE_URL/channels/2253688/feeds.json?"
    const val REQUEST_CHANNEL_FEED = "$BASE_URL/channels/2253688/fields/"
}