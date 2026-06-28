package com.kira.kmp.data.remote

import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.model.Token
import com.kira.kmp.model.request.RefreshRequest
import com.kira.kmp.model.response.ApiResponse
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
                    val oldRefreshToken = oldTokens?.refreshToken
                    if (oldRefreshToken.isNullOrBlank()) return@refreshTokens null
                    try {
                        // IMPORTANT: Use a 'client' instance that does NOT have the Auth plugin installed
                        // to avoid infinite loops, or use the current 'client' (Ktor handles this safely)
                        val response = client.post("auth/refresh") {
                            // Force this request to NOT use the Auth plugin so we don't loop
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(oldRefreshToken))
                        }.body<ApiResponse<Token>>()

                        val tokens = response.data
                        if (tokens != null) {
                            tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                            BearerTokens(tokens.accessToken, tokens.refreshToken)
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
                    val path = request.url.encodedPath
                    val isLoggedIn = tokenManager.getAccessToken() != null
                    !path.contains("auth/") && isLoggedIn
                }
            }
        }

        defaultRequest {
            url(Constants.BASE_URL)
        }
    }
}
