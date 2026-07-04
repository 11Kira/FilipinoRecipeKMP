package com.kira.kmp.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OtpVerificationResponse(
    val resetToken: String
)
