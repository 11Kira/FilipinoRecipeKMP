package com.kira.kmp.features.recipes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.domain.usecase.RecipeUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.enums.ResponseStatus
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    private val recipeUseCase: RecipeUseCase,
    private val userUseCase: UserUseCase,
    private val tokenManager: TokenManager,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _recipeDetailsUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailsUiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(tokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun getRecipeById(recipeId: String) {
        if (_recipeDetailsUiState.value.recipe?.id == recipeId) return
        viewModelScope.launch {
            _recipeDetailsUiState.update { it.copy(isLoading = true) }
            try {
                val response = recipeUseCase.getRecipeById(recipeId)
                if (response.status == ResponseStatus.SUCCESS) {
                    _recipeDetailsUiState.update {
                        it.copy(
                            recipe = response.data,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _recipeDetailsUiState.update { it.copy(error = errorMessage, isLoading = false) }
            }
        }
    }

    fun toggleFavoriteRecipe(recipeId: String) {
        val currentRecipe = _recipeDetailsUiState.value.recipe ?: return
        val wasFavorited = currentRecipe.isFavorited

        _recipeDetailsUiState.update { state ->
            state.copy(recipe = currentRecipe.copy(isFavorited = !wasFavorited))
        }

        viewModelScope.launch {
            try {
                val response = userUseCase.toggleFavoriteRecipe(recipeId)
                if (response.status != ResponseStatus.SUCCESS) {
                    rollbackFavorite(currentRecipe, wasFavorited, "Failed to update favorites.")
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                rollbackFavorite(currentRecipe, wasFavorited, errorMessage)
            }
        }
    }

    private fun rollbackFavorite(
        originalRecipe: Recipe,
        originalState: Boolean,
        errorMessage: String
    ) {
        _recipeDetailsUiState.update {
            it.copy(
                recipe = originalRecipe.copy(isFavorited = originalState),
                error = errorMessage
            )
        }
    }
}
