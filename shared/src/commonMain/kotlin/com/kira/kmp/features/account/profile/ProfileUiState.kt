package com.kira.kmp.features.account.profile

import com.kira.kmp.model.User

data class ProfileUiState(
    val profile: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
