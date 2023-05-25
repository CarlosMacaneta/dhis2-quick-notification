package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class MessagesData(
    @JsonProperty("pager")
    val pager: Pager?,
    @JsonProperty("messageConversations")
    val messageConversations: List<MessageConversation?>?
)