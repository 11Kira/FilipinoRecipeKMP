package com.kira.kmp.features.account.auth.register

data class RegisterUiState(
    val isLoading: Boolean = false
)

sealed interface RegisterUiEffect {
    data object OnSuccessRegistration : RegisterUiEffect
    data class ShowSnackbar(val message: String) : RegisterUiEffect
}