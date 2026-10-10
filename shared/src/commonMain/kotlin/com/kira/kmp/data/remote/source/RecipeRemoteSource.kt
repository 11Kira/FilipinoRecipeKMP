package com.kira.kmp.data.remote.source

import com.kira.kmp.data.remote.service.RecipeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RecipeRemoteSource(
    private val recipeService: RecipeService
) {
    suspend fun getAllRecipes(
        query: String,
        protein: String,
        difficulty: String,
        page: Int,
        size: Int
    ) = withContext(Dispatchers.IO) {
        recipeService.getAllRecipes(
            query,
            protein,
            difficulty,
            page,
            size
        )
    }

    suspend fun getRecipeById(recipeId: String) =
        withContext(Dispatchers.IO) { recipeService.getRecipeById(recipeId) }
}