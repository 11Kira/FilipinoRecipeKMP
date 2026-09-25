package com.kira.kmp.features.account.auth.forgotpassword

data class ForgotPasswordUiState(
    val isLoading: Boolean = false
)

sealed interface ForgotPasswordUiEffect {
    data class ShowSnackbar(val message: String) : ForgotPasswordUiEffect
}