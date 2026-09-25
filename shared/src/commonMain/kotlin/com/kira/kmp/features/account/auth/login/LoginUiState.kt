package com.kira.kmp.features.account.auth.login

data class LoginUiState(
    var isLoading: Boolean = false
)

sealed interface LoginUiEffect {
    data object OnSuccessfulLogin : LoginUiEffect
    data class ShowSnackbar(val message: String) : LoginUiEffect
}
