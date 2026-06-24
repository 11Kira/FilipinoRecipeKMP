package com.kira.kmp.features.account.auth.login

sealed class LoginState {
    data class ShowError(val error: Exception) : LoginState()
    data object OnLogin : LoginState()
}
