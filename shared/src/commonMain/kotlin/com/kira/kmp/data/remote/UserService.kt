package com.kira.kmp.data.remote

import com.kira.kmp.model.Recipe
import com.kira.kmp.model.User
import com.kira.kmp.model.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class UserService(private val httpClient: HttpClient) {
    suspend fun getAllFavoriteRecipes(
        query: String = "",
        page: Int,
        size: Int = 10,
    ): ApiResponse<List<Recipe>> {
        return httpClient.get("users/favorites") {
            parameter("query", query)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun toggleFavoriteRecipe(id: String = ""): ApiResponse<Unit> {
        return httpClient.post("users/favorites/$id").body()
    }

    suspend fun getUserProfile(): ApiResponse<User> {
        return httpClient.get("users/profile").body()
    }
}