package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("lastUpdated")
    val lastUpdated: String?,
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("sender")
    val sender: Sender?,
    @JsonProperty("attachments")
    val attachments: List<Any?>?
)