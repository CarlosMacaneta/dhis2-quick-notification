package org.saudigitus.quicknotification.data.model


import com.fasterxml.jackson.annotation.JsonProperty

data class Pager(
    @JsonProperty("page")
    val page: Int?,
    @JsonProperty("pageCount")
    val pageCount: Int?,
    @JsonProperty("total")
    val total: Int?,
    @JsonProperty("pageSize")
    val pageSize: Int?,
    @JsonProperty("nextPage")
    val nextPage: String?,
    @JsonProperty("prevPage")
    val prevPage: String?
)