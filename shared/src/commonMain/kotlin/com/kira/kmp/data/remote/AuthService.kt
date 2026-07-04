package com.kira.kmp.data.remote

import com.kira.kmp.model.Token
import com.kira.kmp.model.request.ForgotPasswordRequest
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RefreshRequest
import com.kira.kmp.model.request.RegisterRequest
import com.kira.kmp.model.request.ResetPasswordRequest
import com.kira.kmp.model.request.VerifyOtpRequest
import com.kira.kmp.model.response.ApiResponse
import com.kira.kmp.model.response.OtpVerificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthService(private val httpClient: HttpClient) {
    suspend fun register(body: RegisterRequest): ApiResponse<Token> {
        return httpClient.post("auth/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun login(body: LoginRequest): ApiResponse<Token> {
        return httpClient.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun refresh(body: RefreshRequest): ApiResponse<Token> {
        return httpClient.post("auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun logout(body: LogoutRequest): ApiResponse<Unit> {
        return httpClient.post("auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun initiateForgotPassword(email: String): ApiResponse<Unit> {
        return httpClient.post("auth/forgot-password") {
            contentType(ContentType.Application.Json)
            setBody(ForgotPasswordRequest(email))
        }.body()
    }

    suspend fun verifyOtpCode(email: String, otp: String): ApiResponse<OtpVerificationResponse> {
        return httpClient.post("auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(VerifyOtpRequest(email, otp))
        }.body()
    }

    suspend fun resetPassword(request: ResetPasswordRequest): ApiResponse<Unit> {
        return httpClient.post("auth/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}