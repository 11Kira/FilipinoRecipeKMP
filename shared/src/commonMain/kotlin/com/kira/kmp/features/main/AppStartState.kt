package com.kira.kmp.features.main

sealed interface AppStartState {
    object Loading : AppStartState
    object Authenticated : AppStartState
    object Unauthenticated : AppStartState
}