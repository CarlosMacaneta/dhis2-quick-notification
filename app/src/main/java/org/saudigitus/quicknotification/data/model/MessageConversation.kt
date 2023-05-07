package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class MessageConversation(
    @JsonProperty("lastUpdated")
    val lastUpdated: String?,
    @JsonProperty("followUp")
    val followUp: Boolean?,
    @JsonProperty("read")
    val read: Boolean?,
    @JsonProperty("messageCount")
    val messageCount: Int?,
    @JsonProperty("id")
    val id: String?,
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
    val sender: Sender?,
    @JsonProperty("assignee")
    val assignee: Assignee?
)