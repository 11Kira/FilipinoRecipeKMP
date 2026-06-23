package com.kira.kmp.domain.usecase

import com.kira.kmp.data.repository.AuthRepository
import com.kira.kmp.model.Token
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RegisterRequest
import com.kira.kmp.model.response.ApiResponse

class AuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun register(body: RegisterRequest): ApiResponse<Token> {
        return authRepository.register(body)
    }

    suspend fun login(body: LoginRequest): ApiResponse<Token> {
        return authRepository.login(body)
    }

    suspend fun logout(body: LogoutRequest): ApiResponse<Unit> {
        return authRepository.logout(body)
    }
}