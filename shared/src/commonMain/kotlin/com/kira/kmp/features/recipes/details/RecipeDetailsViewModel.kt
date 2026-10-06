package com.kira.kmp.features.recipes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.kmp.domain.usecase.RecipeUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.enums.ResponseStatus
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    private val recipeUseCase: RecipeUseCase,
    private val userUseCase: UserUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _recipeDetailsUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailsUiState.asStateFlow()

    private val _recipeDetailsEffect = Channel<RecipeDetailsUiEffect>(Channel.BUFFERED)
    val recipeDetailsEffect = _recipeDetailsEffect.receiveAsFlow()

    fun getRecipeById(recipeId: String) {
        if (_recipeDetailsUiState.value.recipe?.id == recipeId) return
        viewModelScope.launch {
            _recipeDetailsUiState.update { it.copy(isLoading = true) }
            try {
                val response = recipeUseCase.getRecipeById(recipeId)
                if (response.status == ResponseStatus.SUCCESS) {
                    _recipeDetailsUiState.update {
                        it.copy(recipe = response.data)
                    }
                    syncPendingFavoriteIfNeeded(recipeId)
                } else {
                    _recipeDetailsEffect.send(RecipeDetailsUiEffect.ShowSnackbar(message = response.message.toString()))
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _recipeDetailsEffect.send(RecipeDetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _recipeDetailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun syncPendingFavoriteIfNeeded(recipeId: String) {
        try {
            val entity = recipeUseCase.getRecipeEntity(recipeId)
            if (entity != null && !entity.isFavoriteSynced) {
                val response = userUseCase.toggleFavoriteRecipe(recipeId)
                if (response.status == ResponseStatus.SUCCESS) {
                    recipeUseCase.updateFavoriteStatus(
                        recipeId,
                        entity.isFavorited,
                        isFavoriteSynced = true
                    )
                    println("📡 Pending favorite successfully synced with server for recipe: $recipeId")
                }
            }
        } catch (e: Exception) {
            println("📡 Network sync skipped/failed (offline), will retry later.")
        }
    }

    fun toggleFavoriteRecipe(recipeId: String) {
        val currentRecipe = _recipeDetailsUiState.value.recipe ?: return
        val wasFavorited = currentRecipe.isFavorited
        val newFavoriteState = !wasFavorited

        _recipeDetailsUiState.update { state ->
            state.copy(recipe = currentRecipe.copy(isFavorited = newFavoriteState))
        }

        viewModelScope.launch {
            try {
                recipeUseCase.updateFavoriteStatus(
                    recipeId,
                    newFavoriteState,
                    isFavoriteSynced = false
                )

                val response = userUseCase.toggleFavoriteRecipe(recipeId)
                if (response.status == ResponseStatus.SUCCESS) {
                    recipeUseCase.updateFavoriteStatus(
                        recipeId,
                        newFavoriteState,
                        isFavoriteSynced = true
                    )
                } else {
                    recipeUseCase.updateFavoriteStatus(
                        recipeId,
                        wasFavorited,
                        isFavoriteSynced = true
                    )
                    rollbackFavorite(
                        currentRecipe,
                        wasFavorited,
                        "Failed to sync favorite with server."
                    )
                }
            } catch (e: Exception) {
                println("📡 Network sync failed, favorite saved locally in Room. Will sync when internet comes back.")
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
            )

        }
        viewModelScope.launch {
            _recipeDetailsEffect.send(
                RecipeDetailsUiEffect.ShowSnackbar(message = errorMessage)
            )
        }
    }
}
