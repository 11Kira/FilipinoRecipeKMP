package com.kira.kmp.data.remote

import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.model.request.RefreshRequest
import com.kira.kmp.model.response.ApiResponse
import com.kira.kmp.model.response.RefreshTokenResponse
import com.kira.kmp.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(tokenManager: TokenManager): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(HttpTimeout) {
            connectTimeoutMillis = Constants.CONNECT_TIMEOUT
            requestTimeoutMillis = Constants.READ_TIMEOUT
            socketTimeoutMillis = Constants.WRITE_TIMEOUT
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR_LOG: $message")
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenManager.getAccessToken()
                    val refreshToken = tokenManager.getRefreshToken()
                    if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val oldRefreshToken = oldTokens?.refreshToken ?: tokenManager.getRefreshToken()
                    if (oldRefreshToken.isNullOrBlank()) {
                        tokenManager.clearTokens()
                        return@refreshTokens null
                    }
                    try {
                        val response = client.post("auth/refresh") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(oldRefreshToken))
                        }.body<ApiResponse<RefreshTokenResponse>>()

                        val refreshData = response.data
                        if (refreshData != null) {
                            tokenManager.saveTokens(
                                accessToken = refreshData.accessToken,
                                refreshToken = oldRefreshToken,
                            )
                            BearerTokens(refreshData.accessToken, oldRefreshToken)
                        } else {
                            tokenManager.clearTokens()
                            null
                        }
                    } catch (e: Exception) {
                        tokenManager.clearTokens()
                        null
                    }
                }

                sendWithoutRequest { request ->
                    !request.url.encodedPath.contains("auth/")
                }
            }
        }

        defaultRequest {
            url(Constants.BASE_URL)
        }
    }
}
