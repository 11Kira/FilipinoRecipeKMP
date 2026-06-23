package com.kira.kmp.features.account.auth.register

sealed class RegisterState {
    data class ShowError(val error: Exception) : RegisterState()
    data object OnRegister : RegisterState()
}
