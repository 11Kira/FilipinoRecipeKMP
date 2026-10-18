package com.kira.kmp.features.account.profile

import com.kira.kmp.model.User

data class ProfileUiState(
    val profile: User? = null,
    val isLoading: Boolean = false
)

sealed interface ProfileUiEffect {
    data class ShowSnackbar(val message: String) : ProfileUiEffect
}
