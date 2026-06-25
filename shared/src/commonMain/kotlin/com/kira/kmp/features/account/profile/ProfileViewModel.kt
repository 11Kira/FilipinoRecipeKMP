package com.kira.kmp.features.account.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.model.enums.ResponseStatus
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val tokenManager: TokenManager,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    fun getUserProfile() {
        viewModelScope.launch {
            _profileUiState.update { it.copy(isLoading = true) }
            try {
                val response = userUseCase.getUserProfile()
                if (response.status == ResponseStatus.SUCCESS) {
                    _profileUiState.update {
                        it.copy(
                            profile = response.data,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _profileUiState.update { it.copy(error = errorMessage, isLoading = false) }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                val refreshToken = tokenManager.getRefreshToken() ?: ""
                authUseCase.logout(LogoutRequest(refreshToken))
            } finally {
                tokenManager.clearTokens()
                onLogout.invoke()
            }
        }
    }
}
