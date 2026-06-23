package com.kira.kmp.domain.usecase

import androidx.paging.PagingData
import com.kira.kmp.data.repository.RecipeRepository
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject

class RecipeUseCase(
    private val recipeRepository: RecipeRepository
) {
    fun getAllRecipes(
        query: String,
        protein: String,
        difficulty: String
    ): Flow<PagingData<Recipe>> = recipeRepository.getAllRecipes(query, protein, difficulty)

    suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe> {
        return recipeRepository.getRecipeById(recipeId = recipeId)
    }

    suspend fun saveRecipe(body: JsonObject): ApiResponse<Recipe> {
        return recipeRepository.saveRecipe(body)
    }

    suspend fun updateRecipeById(recipeId: String, body: JsonObject): ApiResponse<Recipe> {
        return recipeRepository.updateRecipeById(recipeId = recipeId, body = body)
    }

    suspend fun deleteRecipeById(recipeId: String) {
        return recipeRepository.deleteRecipeById(recipeId = recipeId)
    }
}