package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    @JsonProperty("displayName")
    val displayName: String?,
    @JsonProperty("id")
    val id: String?
)