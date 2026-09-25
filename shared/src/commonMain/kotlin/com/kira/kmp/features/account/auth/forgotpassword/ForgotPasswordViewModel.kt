package com.kira.kmp.features.account.auth.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.model.enums.ResponseStatus
import com.kira.kmp.model.request.ResetPasswordRequest
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authUseCase: AuthUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {
    private val _currentStep = MutableStateFlow<ForgotPasswordStep>(ForgotPasswordStep.EnterEmail)
    val currentStep: StateFlow<ForgotPasswordStep> = _currentStep

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _forgotPasswordEffect = Channel<ForgotPasswordUiEffect>(Channel.BUFFERED)
    val forgotPasswordEffect = _forgotPasswordEffect.receiveAsFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    var email by mutableStateOf("")
    var otpCode by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    fun updateEmail(newValue: String) {
        email = newValue
    }
    fun updateOtp(newValue: String) {
        otpCode = newValue
    }

    fun updatePassword(newValue: String) {
        password = newValue
    }

    fun updateConfirmPassword(newValue: String) {
        confirmPassword = newValue
    }

    val isEmailValid: Boolean get() = emailRegex.matches(email)
    val isOtpValid: Boolean get() = otpCode.length >= 6
    val isPasswordValid: Boolean get() = password.length >= 6 && password == confirmPassword

    fun requestOtp() {
        if (!isEmailValid || _uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = authUseCase.initiateForgotPassword(email)
                if (response.status == ResponseStatus.SUCCESS) {
                    _currentStep.value = ForgotPasswordStep.EnterOtp(email)
                } else {
                    _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = response.message.toString()))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun verifyOtp() {
        if (!isOtpValid || _uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = authUseCase.verifyOtpCode(email, otpCode)
                if (response.status == ResponseStatus.SUCCESS && response.data != null) {
                    _currentStep.value =
                        ForgotPasswordStep.CreateNewPassword(email, response.data.resetToken)
                } else {
                    _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = response.message.toString()))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun completeReset() {
        if (!isPasswordValid || _uiState.value.isLoading) return
        val currentState = _currentStep.value
        if (currentState !is ForgotPasswordStep.CreateNewPassword) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val request = ResetPasswordRequest(
                    email = currentState.email,
                    resetToken = currentState.token,
                    newPassword = password
                )
                val response = authUseCase.resetPassword(request)
                if (response.status == ResponseStatus.SUCCESS) {
                    _currentStep.value = ForgotPasswordStep.Success
                } else {
                    _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = response.message.toString()))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordEffect.send(ForgotPasswordUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}