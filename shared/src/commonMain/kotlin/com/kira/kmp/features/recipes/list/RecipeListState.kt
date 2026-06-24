package com.kira.kmp.features.recipes.list

import com.kira.kmp.model.Recipe

data class RecipeListState(
    val recipes: List<Recipe>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
