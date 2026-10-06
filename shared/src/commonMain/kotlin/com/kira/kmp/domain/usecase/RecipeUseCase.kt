package com.kira.kmp.domain.usecase

import androidx.paging.PagingData
import com.kira.kmp.data.repository.RecipeRepository
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.response.ApiResponse
import kotlinx.coroutines.flow.Flow

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

    suspend fun updateFavoriteStatus(
        recipeId: String,
        isFavorited: Boolean,
        isFavoriteSynced: Boolean = true
    ) {
        recipeRepository.updateFavoriteStatus(recipeId, isFavorited, isFavoriteSynced)
    }

    suspend fun getRecipeEntity(recipeId: String) = recipeRepository.getRecipeEntity(recipeId)
}