package com.kira.kmp.data.repository

import com.kira.kmp.data.remote.source.AuthRemoteSource
import com.kira.kmp.model.request.LoginRequest
import com.kira.kmp.model.request.LogoutRequest
import com.kira.kmp.model.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRepository(
    private val remoteSource: AuthRemoteSource,
) {
    suspend fun register(body: RegisterRequest) =
        withContext(Dispatchers.IO) { remoteSource.register(body) }

    suspend fun login(body: LoginRequest) =
        withContext(Dispatchers.IO) { remoteSource.login(body) }

    suspend fun logout(body: LogoutRequest) =
        withContext(Dispatchers.IO) { remoteSource.logout(body) }
}