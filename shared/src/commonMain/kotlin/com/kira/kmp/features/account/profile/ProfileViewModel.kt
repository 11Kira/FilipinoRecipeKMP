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
import kotlinx.coroutines.flow.first
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

    init {
        // Automatically pipe database cache mutations directly into the UI state
        viewModelScope.launch {
            userUseCase.userProfileFlow.collect { cachedProfile ->
                _profileUiState.update { it.copy(profile = cachedProfile) }
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            if (_profileUiState.value.profile == null) {
                _profileUiState.update { it.copy(isLoading = true) }
            }
            try {
                val response = userUseCase.refreshUserProfile()
                _profileUiState.update { it.copy(isLoading = false) }

                if (response.status != ResponseStatus.SUCCESS) {
                    _profileUiState.update { it.copy(error = response.message) }
                }
            } catch (e: Exception) {
                _profileUiState.update { it.copy(isLoading = false) }
                val cachedProfile = userUseCase.userProfileFlow.first()
                if (cachedProfile == null) {
                    val errorMessage = networkUtils.parseNetworkError(e)
                    _profileUiState.update { it.copy(error = errorMessage) }
                } else {
                    println("📡 Network sync failed, but profile cache exists. Suppressing error snackbar.")
                }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                val refreshToken = tokenManager.getRefreshToken() ?: ""
                authUseCase.logout(LogoutRequest(refreshToken))
            } finally {
                userUseCase.clearLocalProfile()
                tokenManager.clearTokens()
                authUseCase.clearNetworkSession()
                onLogout.invoke()
            }
        }
    }
}
