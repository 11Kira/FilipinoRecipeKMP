package com.kira.kmp.data.repository

import com.kira.kmp.data.remote.source.AuthRemoteSource
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RegisterRequest
import com.kira.kmp.model.request.ResetPasswordRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.clearAuthTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRepository(
    private val remoteSource: AuthRemoteSource,
    private val httpClient: HttpClient
) {
    suspend fun register(body: RegisterRequest) =
        withContext(Dispatchers.IO) { remoteSource.register(body) }

    suspend fun login(body: LoginRequest) =
        withContext(Dispatchers.IO) { remoteSource.login(body) }

    suspend fun logout(body: LogoutRequest) =
        withContext(Dispatchers.IO) { remoteSource.logout(body) }

    suspend fun initiateForgotPassword(email: String) =
        withContext(Dispatchers.IO) { remoteSource.initiateForgotPassword(email) }

    suspend fun verifyOtpCode(email: String, otp: String) =
        withContext(Dispatchers.IO) { remoteSource.verifyOtpCode(email, otp) }

    suspend fun resetPassword(request: ResetPasswordRequest) =
        withContext(Dispatchers.IO) { remoteSource.resetPassword(request) }

    fun clearKtorAuthCache() {
        httpClient.clearAuthTokens()
    }
}