package com.kira.kmp.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("totalElements")
    val totalElements: Long,
    @SerialName("hasNext")
    val hasNext: Boolean,
)
