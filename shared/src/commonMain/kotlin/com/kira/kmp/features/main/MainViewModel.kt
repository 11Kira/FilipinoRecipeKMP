package com.kira.kmp.features.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _startState = MutableStateFlow<AppStartState>(AppStartState.Loading)
    val startState: StateFlow<AppStartState> = _startState.asStateFlow()

    var isBottomNavExpanded by mutableStateOf(true)

    private val _scrollToTopEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val scrollToTopEvent = _scrollToTopEvent.asSharedFlow()

    fun triggerScrollToTop(targetTab: String) {
        viewModelScope.launch { _scrollToTopEvent.emit(targetTab) }
    }

    init {
        refreshSession()
    }

    fun refreshSession() {
        viewModelScope.launch {
            val isLoggedIn = tokenManager.isLoggedIn()
            if (isLoggedIn) {
                _startState.value = AppStartState.Authenticated
            } else {
                _startState.value = AppStartState.Unauthenticated
            }
        }
    }
}