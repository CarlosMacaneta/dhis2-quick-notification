package org.saudigitus.quicknotification.data.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
@Entity("message_conversations")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class MessageConversation(
    @JsonProperty("lastUpdated")
    @ColumnInfo("message_update")
    val lastUpdated: String?,
    @JsonProperty("followUp")
    val followUp: Boolean?,
    @JsonProperty("read")
    val read: Boolean?,
    @JsonProperty("messageCount")
    val messageCount: Int? = 0,
    @JsonProperty("id")
    @PrimaryKey val id: String,
    @JsonProperty("messageType")
    val messageType: String?,
    @JsonProperty("subject")
    val subject: String?,
    @JsonProperty("lastMessage")
    val lastMessage: String?,
    @JsonProperty("priority")
    val priority: String?,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("lastSender")
    @Embedded val sender: Sender?,
    @JsonProperty("assignee")
    @Embedded val assignee: Assignee?
) {
    constructor() : this(
        null,
        false,
        false,
        0,
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}