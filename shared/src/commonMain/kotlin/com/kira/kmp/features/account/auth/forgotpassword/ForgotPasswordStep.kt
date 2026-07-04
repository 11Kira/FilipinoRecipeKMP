package com.kira.kmp.features.account.auth.forgotpassword

sealed interface ForgotPasswordStep {
    object EnterEmail : ForgotPasswordStep
    data class EnterOtp(val email: String) : ForgotPasswordStep
    data class CreateNewPassword(val email: String, val token: String) : ForgotPasswordStep
    object Success : ForgotPasswordStep
}