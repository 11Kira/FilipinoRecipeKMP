package com.kira.kmp.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class User(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("role")
    val role: String,
)