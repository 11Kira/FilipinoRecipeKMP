package com.kira.kmp.features.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kira.kmp.domain.usecase.RecipeUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.utils.NetworkUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

class RecipeListViewModel(
    private val recipeUseCase: RecipeUseCase,
    private val userUseCase: UserUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _recipeListUiState = MutableStateFlow(RecipeListState())
    val recipeListUiState = _recipeListUiState.asStateFlow()
    private val _filterTrigger = MutableStateFlow(0)

    private val _selectedProteins = MutableStateFlow<Set<String>>(emptySet())
    val selectedProteins = _selectedProteins.asStateFlow()
    private val _appliedProteins = MutableStateFlow<Set<String>>(emptySet())

    private val _selectedDifficulties = MutableStateFlow<Set<String>>(emptySet())
    val selectedDifficulties = _selectedDifficulties.asStateFlow()
    private val _appliedDifficulties = MutableStateFlow<Set<String>>(emptySet())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val appliedFilterCount =
        combine(_appliedProteins, _appliedDifficulties) { proteins, difficulties ->
            proteins.size + difficulties.size
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val recipePagingFlow = combine(
        _searchQuery.debounce(500L.milliseconds),
        _filterTrigger,
        _appliedProteins,
        _appliedDifficulties
    ) { query, _, proteins, difficulties ->
        val proteinQuery = proteins.joinToString(",").lowercase()
        val difficultyQuery = difficulties.joinToString(",").lowercase()
        Triple(query, proteinQuery, difficultyQuery)
    }.distinctUntilChanged().flatMapLatest { (query, proteins, difficulties) ->
        recipeUseCase.getAllRecipes(
            query = query,
            protein = proteins,
            difficulty = difficulties
        )
    }.cachedIn(viewModelScope)

    fun applyFilters() {
        _appliedProteins.value = _selectedProteins.value
        _appliedDifficulties.value = _selectedDifficulties.value
        _filterTrigger.value += 1
    }

    fun syncSelectedWithApplied() {
        _selectedProteins.value = _appliedProteins.value
        _selectedDifficulties.value = _appliedDifficulties.value
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleProtein(protein: String) {
        _selectedProteins.update { current ->
            if (current.contains(protein)) current - protein else current + protein
        }
    }

    fun toggleDifficulty(difficulty: String) {
        _selectedDifficulties.update { current ->
            if (current.contains(difficulty)) current - difficulty else current + difficulty
        }
    }

    fun resetFilters() {
        _selectedProteins.value = emptySet()
        _selectedDifficulties.value = emptySet()
    }
}