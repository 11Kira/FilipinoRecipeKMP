package com.kira.kmp.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val resetToken: String,
    val newPassword: String
)
