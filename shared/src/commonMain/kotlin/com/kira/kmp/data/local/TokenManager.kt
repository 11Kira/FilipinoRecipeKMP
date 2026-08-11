package com.kira.kmp.data.local

class TokenManager(private val storage: EncryptedStorage) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        storage.putString(KEY_ACCESS_TOKEN, accessToken)
        storage.putString(KEY_REFRESH_TOKEN, refreshToken)
    }

    suspend fun getAccessToken(): String? = storage.getString(KEY_ACCESS_TOKEN)
    suspend fun getRefreshToken(): String? = storage.getString(KEY_REFRESH_TOKEN)

    suspend fun isLoggedIn(): Boolean = getAccessToken() != null

    suspend fun clearTokens() {
        storage.clear()
    }
}