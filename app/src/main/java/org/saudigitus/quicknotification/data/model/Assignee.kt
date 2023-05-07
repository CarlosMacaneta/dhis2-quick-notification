package org.saudigitus.quicknotification.data.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Assignee(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("displayName")
    val displayName: String?
)
