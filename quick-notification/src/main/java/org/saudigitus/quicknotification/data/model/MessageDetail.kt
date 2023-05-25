package org.saudigitus.quicknotification.data.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
@Entity("message_details")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class MessageDetail(
    @JsonProperty("read")
    val read: Boolean?,
    @JsonProperty("id")
    @PrimaryKey var id: String,
    @JsonProperty("messageType")
    val messageType: String?,
    @JsonProperty("subject")
    val subject: String?,
    @JsonProperty("lastMessage")
    val lastMessage: String?,
    @JsonProperty("priority")
    val priority: String?,
    @JsonProperty("favorite")
    val favorite: Boolean? = false,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("assignee")
    @Embedded val assignee: Assignee?,
    @JsonProperty("userMessages")
    @Embedded val userMessages: List<UserMessage?>,
    @JsonProperty("messages")
    @Embedded val messages: List<Message?>
) {
    constructor() : this(
        false,
        "",
        null,
        null,
        null,
        null,
        false,
        null,
        null,
        listOf(),
        listOf(),
    )
}