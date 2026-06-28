package com.kira.kmp.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ResponseStatusSerializer::class)
enum class ResponseStatus {
    @SerialName("SUCCESS")
    SUCCESS,
    @SerialName("FAILED")
    FAILED,
    @SerialName("UNKNOWN")
    UNKNOWN
}