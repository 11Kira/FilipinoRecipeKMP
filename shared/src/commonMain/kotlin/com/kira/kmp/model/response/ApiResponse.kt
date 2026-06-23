package com.kira.kmp.model.response

import com.kira.kmp.model.enums.ResponseStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("status")
    val status: ResponseStatus,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
    @SerialName("paging")
    val paging: PagingResponse? = null
)