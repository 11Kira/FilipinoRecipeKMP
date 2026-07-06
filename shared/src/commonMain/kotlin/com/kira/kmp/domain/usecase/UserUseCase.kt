package com.kira.kmp.domain.usecase

import androidx.paging.PagingData
import com.kira.kmp.data.repository.UserRepository
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.User
import com.kira.kmp.model.response.ApiResponse
import kotlinx.coroutines.flow.Flow

class UserUseCase(
    private val userRepository: UserRepository
) {
    val userProfileFlow: Flow<User?> = userRepository.userProfileFlow

    suspend fun refreshUserProfile(): ApiResponse<User> {
        return userRepository.refreshUserProfile()
    }

    suspend fun clearLocalProfile() {
        userRepository.clearLocalProfile()
    }

    fun getAllFavoriteRecipes(
        query: String,
    ): Flow<PagingData<Recipe>> = userRepository.getAllFavoriteRecipes(query)

    suspend fun toggleFavoriteRecipe(recipeId: String): ApiResponse<Unit> {
        return userRepository.toggleFavoriteRecipe(recipeId)
    }

    suspend fun getUserProfile() = userRepository.getUserProfile()
}