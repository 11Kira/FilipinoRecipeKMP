package com.kira.kmp.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("total")
    val total: Int,
    @SerialName("next")
    val next: String? = "",
    @SerialName("previous")
    val previous: String? = "",
)