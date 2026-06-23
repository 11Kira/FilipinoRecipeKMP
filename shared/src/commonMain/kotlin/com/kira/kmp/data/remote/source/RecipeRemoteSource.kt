package com.kira.kmp.data.remote.source

import com.kira.kmp.data.remote.RecipeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject

class RecipeRemoteSource(
    private val recipeService: RecipeService
) {
    suspend fun getAllRecipes(
        query: String,
        protein: String,
        difficulty: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        recipeService.getAllRecipes(
            query,
            protein,
            difficulty,
            page
        )
    }

    suspend fun getRecipeById(recipeId: String) =
        withContext(Dispatchers.IO) { recipeService.getRecipeById(recipeId) }

    suspend fun deleteRecipeById(recipeId: String) =
        withContext(Dispatchers.IO) { recipeService.deleteRecipeById(recipeId) }

    suspend fun saveRecipe(body: JsonObject) =
        withContext(Dispatchers.IO) { recipeService.saveRecipe(body) }

    suspend fun updateRecipeById(recipeId: String, body: JsonObject) =
        withContext(Dispatchers.IO) { recipeService.updateRecipe(recipeId, body) }
}