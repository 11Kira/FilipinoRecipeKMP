package com.kira.kmp.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatus {
    SUCCESS,
    FAILED
}