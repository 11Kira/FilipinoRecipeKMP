package com.kira.kmp.features.splash

sealed class StartDestination {
    data object Home : StartDestination()
    data object Login : StartDestination()
}
