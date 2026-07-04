package com.kira.kmp.features.account.auth.forgotpassword

sealed class ForgotPasswordState {
    data class ShowError(val error: Exception) : ForgotPasswordState()
}