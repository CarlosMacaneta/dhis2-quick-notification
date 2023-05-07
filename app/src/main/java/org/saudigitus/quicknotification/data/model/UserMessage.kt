package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class UserMessage(
    @JsonProperty("user")
    val user: User?
)