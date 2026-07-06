package com.kira.kmp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kira.kmp.data.local.RecipeDao
import com.kira.kmp.data.remote.source.RecipePagingSource
import com.kira.kmp.data.remote.source.RecipeRemoteSource
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject

class RecipeRepository(
    private val recipeRemoteSource: RecipeRemoteSource,
    private val recipeDao: RecipeDao
) {
    fun getAllRecipes(
        query: String,
        protein: String,
        difficulty: String,
    ): Flow<PagingData<Recipe>> =
        Pager(
            PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            RecipePagingSource(
                remoteSource = recipeRemoteSource,
                query = query,
                protein = protein,
                difficulty = difficulty,
            )
        }.flow

    suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe> {
        return recipeRemoteSource.getRecipeById(recipeId = recipeId)
    }

    suspend fun saveRecipe(body: JsonObject): ApiResponse<Recipe> {
        return recipeRemoteSource.saveRecipe(body)
    }

    suspend fun updateRecipeById(recipeId: String, body: JsonObject): ApiResponse<Recipe> {
        return recipeRemoteSource.updateRecipeById(recipeId = recipeId, body = body)
    }

    suspend fun deleteRecipeById(recipeId: String) {
        return recipeRemoteSource.deleteRecipeById(recipeId = recipeId)
    }
}