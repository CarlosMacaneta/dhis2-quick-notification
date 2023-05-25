package org.saudigitus.quicknotification.data.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class UserMessage(
    @JsonProperty("user")
    val user: User?
) {
    constructor(): this(
        null
    )
}