package com.kira.kmp.data.remote.service

import com.kira.kmp.model.Recipe
import com.kira.kmp.model.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipeService(private val httpClient: HttpClient) {
    suspend fun getAllRecipes(
        query: String = "",
        protein: String = "",
        difficulty: String = "",
        page: Int,
        size: Int = 10,
    ): ApiResponse<List<Recipe>> {
        return httpClient.get("recipes") {
            parameter("query", query)
            parameter("protein", protein)
            parameter("difficulty", difficulty)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getRecipeById(id: String): ApiResponse<Recipe> {
        return httpClient.get("recipes/$id").body()
    }
}