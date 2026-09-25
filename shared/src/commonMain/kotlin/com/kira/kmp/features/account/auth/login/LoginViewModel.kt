package com.kira.kmp.features.account.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _loginEffect = Channel<LoginUiEffect>(Channel.BUFFERED)
    val loginEffect: Flow<LoginUiEffect> = _loginEffect.receiveAsFlow()

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
        if (!isInputValid || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = authUseCase.login(LoginRequest(email, password))
                val tokens = response.data
                if (tokens != null) {
                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                    _loginEffect.send(LoginUiEffect.OnSuccessfulLogin)
                } else {
                    _loginEffect.send(
                        LoginUiEffect.ShowSnackbar(
                            message = "Login failed. Please try again."
                        )
                    )
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _loginEffect.send(LoginUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
