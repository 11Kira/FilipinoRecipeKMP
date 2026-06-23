package com.kira.kmp.features.recipes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.domain.usecase.RecipeUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.enums.ResponseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    private val recipeUseCase: RecipeUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _recipeDetailsUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailsUiState.asStateFlow()

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
                _recipeDetailsUiState.update { it.copy(error = e.message, isLoading = false) }
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
                    rollbackFavorite(currentRecipe, wasFavorited)
                }
            } catch (e: Exception) {
                rollbackFavorite(currentRecipe, wasFavorited)
            }
        }
    }

    private fun rollbackFavorite(originalRecipe: Recipe, originalState: Boolean) {
        _recipeDetailsUiState.update {
            it.copy(
                recipe = originalRecipe.copy(isFavorited = originalState),
                error = "Failed to update favorites. Please check your connection."
            )
        }
    }
}
