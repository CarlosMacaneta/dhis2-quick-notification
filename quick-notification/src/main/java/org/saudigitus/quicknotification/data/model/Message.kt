package org.saudigitus.quicknotification.data.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @JsonProperty("lastUpdated")
    val lastUpdated: String? = "",
    @JsonProperty("id")
    @ColumnInfo("message_id")
    var id: String = "",
    @JsonProperty("text")
    val text: String? = "",
    @JsonProperty("sender")
    @Embedded val sender: Sender?,
    @JsonProperty("attachments")
    val attachments: List<@Contextual Any?>
) {
    constructor(): this(
        null,
        "",
        null,
        null,
        listOf(),
    )
}