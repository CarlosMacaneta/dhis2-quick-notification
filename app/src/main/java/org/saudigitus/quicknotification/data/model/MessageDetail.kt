package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class MessageDetail(
    @JsonProperty("read")
    val read: Boolean? = null,
    @JsonProperty("id")
    val id: String? = null,
    @JsonProperty("messageType")
    val messageType: String? = null,
    @JsonProperty("subject")
    val subject: String? = null,
    @JsonProperty("lastMessage")
    val lastMessage: String? = null,
    @JsonProperty("priority")
    val priority: String? = null,
    @JsonProperty("favorite")
    val favorite: Boolean? = null,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("assignee")
    val assignee: Assignee? = null,
    @JsonProperty("userMessages")
    val userMessages: List<UserMessage?>? = null,
    @JsonProperty("messages")
    val messages: List<Message?>? = null
)