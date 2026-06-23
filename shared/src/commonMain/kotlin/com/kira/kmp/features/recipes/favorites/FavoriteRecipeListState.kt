package com.kira.kmp.features.recipes.favorites

import com.kira.kmp.model.Recipe

data class FavoriteRecipeListState(
    val recipes: List<Recipe>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
