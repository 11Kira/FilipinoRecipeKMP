package com.kira.kmp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _scrollToTopEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val scrollToTopEvent = _scrollToTopEvent.asSharedFlow()

    fun triggerScrollToTop(targetTab: String) {
        viewModelScope.launch {
            _scrollToTopEvent.emit(targetTab)
        }
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
