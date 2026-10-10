package com.kira.kmp.features.recipes.details

import com.kira.kmp.model.Recipe

data class RecipeDetailsUiState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false
)

sealed interface RecipeDetailsUiEffect {
    data class ShowSnackbar(val message: String) : RecipeDetailsUiEffect
}
