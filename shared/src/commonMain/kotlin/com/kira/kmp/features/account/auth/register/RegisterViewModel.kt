package com.kira.kmp.features.account.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.model.request.RegisterRequest
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager,
    private val networkUtils: NetworkUtils
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _registerEffect = Channel<RegisterUiEffect>(Channel.BUFFERED)
    val registerEffect: Flow<RegisterUiEffect> = _registerEffect.receiveAsFlow()

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    val isInputValid: Boolean
        get() = username.isNotBlank() &&
                emailRegex.matches(email) &&
                password.length >= 6 &&
                password == confirmPassword

    fun register(email: String, password: String, username: String) {
        if (!isInputValid || _uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = authUseCase.register(RegisterRequest(email, password, username))
                val tokens = response.data
                if (tokens != null) {
                    tokenManager.saveTokens(
                        tokens.accessToken,
                        tokens.refreshToken
                    )
                    _registerEffect.send(RegisterUiEffect.OnSuccessRegistration)
                } else {
                    _registerEffect.send(RegisterUiEffect.ShowSnackbar(message = response.message.toString()))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _registerEffect.send(RegisterUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
