package com.kira.kmp.data.remote.source

import com.kira.kmp.data.remote.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRemoteSource(
    private val userService: UserService
) {
    suspend fun getAllFavoriteRecipes(
        query: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        userService.getAllFavoriteRecipes(
            query,
            page
        )
    }

    suspend fun toggleFavoriteRecipe(
        recipeId: String
    ) = withContext(Dispatchers.IO) {
        userService.toggleFavoriteRecipe(
            recipeId
        )
    }

    suspend fun getUserProfile() = withContext(Dispatchers.IO) { userService.getUserProfile() }
}