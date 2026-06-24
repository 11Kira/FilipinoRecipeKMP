package com.kira.kmp.features.account.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.model.request.LoginRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState: MutableSharedFlow<LoginState> = MutableSharedFlow()
    val loginState
        get() = _loginState.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun updateEmail(newValue: String) {
        email = newValue
    }

    fun updatePassword(newValue: String) {
        password = newValue
    }

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    val isInputValid: Boolean
        get() = emailRegex.matches(email) && password.length >= 6

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authUseCase.login(LoginRequest(email, password))
                val tokens = response.data
                if (tokens != null) {
                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                    _loginState.emit(LoginState.OnLogin)
                } else {
                    _loginState.emit(LoginState.ShowError(Exception("Invalid response from server")))
                }
            } catch (e: Exception) {
                _loginState.emit(LoginState.ShowError(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
