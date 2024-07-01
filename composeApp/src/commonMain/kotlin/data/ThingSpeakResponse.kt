package data

import kotlinx.serialization.Serializable

@Serializable
data class ThingSpeakResponse(
    val channel: ChannelThingSpeak? = null,
    val feeds: List<FeedsThingSpeak?> = emptyList()
)

@Serializable
data class ChannelThingSpeak(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val field1: String,
    val field2: String,
    val field3: String,
    val field4: String,
    val field5: String,
    val field6: String,
    val field7: String,
    val field8: String,
    val created_at: String,
    val updated_at: String,
    val last_entry_id: Int,
)

@Serializable
data class FeedsThingSpeak(
    val created_at: String,
    val entry_id: Int,
    val field1: String? = null,
    val field2: String? = null,
    val field3: String? = null,
    val field4: String? = null,
    val field5: String? = null,
    val field6: String? = null,
    val field7: String? = null,
    val field8: String? = null,
)

@Serializable
data class Feeds(
    var fieldId: String? = null,
    var fieldType: String? = null,
    var fieldName: String? = null,
    var fieldValue: String? = null,
    var fieldData: String? = null,
)