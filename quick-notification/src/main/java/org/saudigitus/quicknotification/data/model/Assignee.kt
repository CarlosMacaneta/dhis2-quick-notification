package org.saudigitus.quicknotification.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class Assignee(
    @JsonProperty("id")
    @ColumnInfo("assignee_id")
    val id: String,
    @JsonProperty("displayName")
    @ColumnInfo("assignee_name")
    val displayName: String?
) {
    constructor(): this(
        id ="",
        displayName = null
    )
}
