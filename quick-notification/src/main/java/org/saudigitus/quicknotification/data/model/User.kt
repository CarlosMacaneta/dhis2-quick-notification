package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @JsonProperty("displayName")
    val displayName: String? = "",
    @JsonProperty("id")
    var id: String = ""
) {
    constructor(): this(
        null,
        ""
    )
}