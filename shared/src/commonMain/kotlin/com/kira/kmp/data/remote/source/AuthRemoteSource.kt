package com.kira.kmp.data.remote.source

import com.kira.kmp.data.remote.AuthService
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRemoteSource(
    private val authService: AuthService
) {
    suspend fun register(body: RegisterRequest) = withContext(Dispatchers.IO) {
        authService.register(body)
    }

    suspend fun login(body: LoginRequest) = withContext(Dispatchers.IO) {
        authService.login(body)
    }

    suspend fun logout(body: LogoutRequest) = withContext(Dispatchers.IO) {
        authService.logout(body)
    }
}