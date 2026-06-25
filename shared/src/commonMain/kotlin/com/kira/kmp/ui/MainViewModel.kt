package com.kira.kmp.ui

import androidx.lifecycle.ViewModel
import com.kira.kmp.data.local.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private var _currentlySelectedTab = MutableStateFlow("Recipes")
    val currentlySelectedTab: StateFlow<String> = _currentlySelectedTab.asStateFlow()

    fun updateSelectedTab(selectedTab: String) {
        _currentlySelectedTab.value = selectedTab
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
