package com.kira.kmp.data.remote

import com.kira.kmp.model.Recipe
import com.kira.kmp.model.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject

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

    suspend fun saveRecipe(body: JsonObject): ApiResponse<Recipe> {
        return httpClient.post("recipes") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun updateRecipe(id: String, body: JsonObject): ApiResponse<Recipe> {
        return httpClient.put("recipes/$id") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun deleteRecipeById(id: String) {
        httpClient.delete("recipes/$id")
    }
}