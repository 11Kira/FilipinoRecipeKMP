package com.kira.kmp.utils

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class NetworkUtils {
    suspend fun parseNetworkError(e: Exception): String {
        return when (e) {
            is ResponseException -> {
                try {
                    val errorBody = e.response.bodyAsText()
                    if (errorBody.isNotBlank()) {
                        val jsonElement = Json.parseToJsonElement(errorBody)
                        jsonElement.jsonObject["message"]?.jsonPrimitive?.content
                            ?: "An unexpected server error occurred."
                    } else {
                        "An unexpected server error occurred."
                    }
                } catch (parseException: Exception) {
                    "Failed to parse server response."
                }
            }
            // In KMP with Ktor, IO exceptions (like No Internet) depend on the engine, 
            // but generally, we can catch general exceptions or specific Ktor ones.
            else -> {
                val message = e.message ?: ""
                if (message.contains("ConnectException") || message.contains("timed out")) {
                    "Network error. Please check your internet connection."
                } else {
                    e.message ?: "An unknown error occurred."
                }
            }
        }
    }
}
