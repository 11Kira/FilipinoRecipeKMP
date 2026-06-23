package com.kira.kmp.features.recipes.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.model.enums.ResponseStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteRecipeListViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _favoriteRecipeListUiState = MutableStateFlow(FavoriteRecipeListState())
    val favoriteRecipeListUiState = _favoriteRecipeListUiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val favoritePagingFlow = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            userUseCase.getAllFavoriteRecipes(query = query)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleFavorite(recipeId: String) {
        val currentList = _favoriteRecipeListUiState.value.recipes ?: emptyList()
        val recipeIndex = currentList.indexOfFirst { it.id == recipeId }
        if (recipeIndex == -1) return

        val originalRecipe = currentList[recipeIndex]
        val wasFavorited = originalRecipe.isFavorited

        _favoriteRecipeListUiState.update { state ->
            val updatedList = currentList.toMutableList().apply {
                this[recipeIndex] = originalRecipe.copy(isFavorited = !wasFavorited)
            }
            state.copy(recipes = updatedList)
        }

        viewModelScope.launch {
            try {
                val response = userUseCase.toggleFavoriteRecipe(recipeId)
                if (response.status != ResponseStatus.SUCCESS) {
                    rollbackListFavorite(recipeId, wasFavorited)
                }
            } catch (e: Exception) {
                rollbackListFavorite(recipeId, wasFavorited)
            }
        }
    }

    private fun rollbackListFavorite(recipeId: String, wasFavorited: Boolean) {
        // Implementation for rollback if needed
    }
}
