package com.kira.kmp.domain.usecase

import com.kira.kmp.data.repository.AuthRepository
import com.kira.kmp.model.Token
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RegisterRequest
import com.kira.kmp.model.request.ResetPasswordRequest
import com.kira.kmp.model.response.ApiResponse
import com.kira.kmp.model.response.OtpVerificationResponse

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

    suspend fun initiateForgotPassword(email: String): ApiResponse<Unit> {
        return authRepository.initiateForgotPassword(email)
    }

    suspend fun verifyOtpCode(email: String, otp: String): ApiResponse<OtpVerificationResponse> {
        return authRepository.verifyOtpCode(email, otp)
    }

    suspend fun resetPassword(request: ResetPasswordRequest): ApiResponse<Unit> {
        return authRepository.resetPassword(request)
    }

    fun clearNetworkSession() {
        authRepository.clearKtorAuthCache()
    }
}