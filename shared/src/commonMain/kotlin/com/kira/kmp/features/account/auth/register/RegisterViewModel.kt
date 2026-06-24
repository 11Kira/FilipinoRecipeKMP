package com.kira.kmp.features.account.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.model.request.RegisterRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _registerState: MutableSharedFlow<RegisterState> = MutableSharedFlow()
    val registerState
        get() = _registerState.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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
        if (!isInputValid) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authUseCase.register(RegisterRequest(email, password, username))
                _registerState.emit(RegisterState.OnRegister)
            } catch (e: Exception) {
                _registerState.emit(RegisterState.ShowError(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
