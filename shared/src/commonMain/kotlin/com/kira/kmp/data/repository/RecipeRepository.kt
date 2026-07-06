package com.kira.kmp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kira.kmp.data.local.recipe.RecipeDao
import com.kira.kmp.data.local.recipe.RecipeRemoteMediator
import com.kira.kmp.data.local.recipe.toDomain
import com.kira.kmp.data.local.recipe.toEntity
import com.kira.kmp.data.remote.source.RecipeRemoteSource
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.enums.ResponseStatus
import com.kira.kmp.model.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonObject

class RecipeRepository(
    private val recipeRemoteSource: RecipeRemoteSource,
    private val recipeDao: RecipeDao
) {
    @OptIn(ExperimentalPagingApi::class)
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
            ),
            remoteMediator = RecipeRemoteMediator(
                query = query,
                protein = protein,
                difficulty = difficulty,
                remoteSource = recipeRemoteSource,
                recipeDao = recipeDao
            ),
            pagingSourceFactory = {
                recipeDao.getAllRecipesPaging(query = "%$query%")
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

    suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe> {
        try {
            val cachedEntity = recipeDao.getRecipeById(recipeId)
            if (cachedEntity != null) {
                return ApiResponse(
                    status = ResponseStatus.SUCCESS,
                    message = "Served from local cache",
                    data = cachedEntity.toDomain()
                )
            }

            val remoteResponse = recipeRemoteSource.getRecipeById(recipeId)

            if (remoteResponse.status == ResponseStatus.SUCCESS && remoteResponse.data != null) {
                recipeDao.insertRecipes(listOf(remoteResponse.data.toEntity()))
            }

            return remoteResponse
        } catch (e: Exception) {
            return ApiResponse(
                status = ResponseStatus.FAILED,
                message = e.message ?: "An unexpected error occurred"
            )
        }
    }

    suspend fun updateFavoriteStatus(recipeId: String, isFavorited: Boolean) {
        recipeDao.updateFavoriteStatus(recipeId, isFavorited)
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