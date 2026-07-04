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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authUseCase: AuthUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {
    private val _currentStep = MutableStateFlow<ForgotPasswordStep>(ForgotPasswordStep.EnterEmail)
    val currentStep: StateFlow<ForgotPasswordStep> = _currentStep

    private val _forgotPasswordState: MutableSharedFlow<ForgotPasswordState> = MutableSharedFlow()
    val forgotPasswordState
        get() = _forgotPasswordState.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    fun updateEmail(newValue: String) {
        email = newValue
    }

    val isEmailValid: Boolean
        get() = emailRegex.matches(email)
    val isPasswordValid: Boolean
        get() = password.length >= 6 &&
                password == confirmPassword

    fun requestOtp(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authUseCase.initiateForgotPassword(email)
                if (response.status == ResponseStatus.SUCCESS) {
                    _currentStep.value = ForgotPasswordStep.EnterOtp(email)
                } else {
                    _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(response.message)))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(errorMessage)))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authUseCase.verifyOtpCode(email, otp)
                if (response.status == ResponseStatus.SUCCESS && response.data != null) {
                    _currentStep.value =
                        ForgotPasswordStep.CreateNewPassword(email, response.data.resetToken)
                } else {
                    _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(response.message)))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(errorMessage)))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun completeReset(request: ResetPasswordRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authUseCase.resetPassword(request)
                if (response.status == ResponseStatus.SUCCESS) {
                    _currentStep.value = ForgotPasswordStep.Success
                } else {
                    _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(response.message)))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _forgotPasswordState.emit(ForgotPasswordState.ShowError(Exception(errorMessage)))
            } finally {
                _isLoading.value = false
            }
        }
    }
}