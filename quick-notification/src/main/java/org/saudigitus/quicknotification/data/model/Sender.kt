package org.saudigitus.quicknotification.data.model


import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class Sender(
    @JsonProperty("id")
    @ColumnInfo("sender_id")
    var id: String,
    @JsonProperty("displayName")
    @ColumnInfo("sender_name")
    val displayName: String?
) {
    constructor(): this(
        "",
        null
    )
}