package com.kira.kmp.features.recipes.list

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.ui.component.FilterSheetContent
import com.kira.kmp.ui.component.recipe.RecipeBaseScreen
import com.kira.kmp.ui.component.recipe.RecipeFilter
import com.kira.kmp.utils.ColorUtils
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeListScreen(
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: RecipeListViewModel = koinViewModel(),
    mainViewModel: MainViewModel,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue -> newValue != SheetValue.Hidden })
    val recipes = viewModel.recipePagingFlow.collectAsLazyPagingItems()
    val refreshState = recipes.loadState.refresh
    var isManualRefresh by remember { mutableStateOf(false) }
    val isRefreshing = refreshState is LoadState.Loading && isManualRefresh

    LaunchedEffect(recipes.loadState.append) {
        val append = recipes.loadState.append
        if (append is LoadState.Error) {
            onShowSnackbar("Failed to load more recipes")
        }
    }
    val query by viewModel.searchQuery.collectAsState()
    var lastScrolledQuery by rememberSaveable { mutableStateOf("") }
    var hasShownOfflineSnackbar by rememberSaveable { mutableStateOf(false) }
    val selectedProteins by viewModel.selectedProteins.collectAsState()
    val selectedDifficulties by viewModel.selectedDifficulties.collectAsState()
    val appliedFilterCount by viewModel.appliedFilterCount.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

    LaunchedEffect(refreshState) {
        when (refreshState) {
            is LoadState.NotLoading -> {
                hasShownOfflineSnackbar = false
                if (recipes.itemCount > 0 && query != lastScrolledQuery) {
                    listState.scrollToItem(0)
                    lastScrolledQuery = query
                }
            }

            is LoadState.Error -> {
                if (recipes.itemCount > 0 && !hasShownOfflineSnackbar) {
                    onShowSnackbar("Offline mode: Displaying cached recipes.")
                    hasShownOfflineSnackbar = true
                }
            }

            is LoadState.Loading -> {
                hasShownOfflineSnackbar = false
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isManualRefresh = true
            recipes.refresh()
        }
    ) {
        RecipeBaseScreen(
            recipes = recipes,
            query = query,
            listState = listState,
            mainViewModel = mainViewModel,
            screenLabel = "Recipes",
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            onItemClick = onItemClick,
            contentPadding = contentPadding,
            searchHint = "Search recipes...",
            actionSlot = {
                RecipeFilter(
                    onButtonClick = {
                        viewModel.syncSelectedWithApplied()
                        showFilterSheet = true
                    },
                    appliedFilterCount = appliedFilterCount
                )
            }
        )
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = ColorUtils().pastelMint,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            FilterSheetContent(
                proteins = listOf("Pork", "Beef", "Chicken", "Seafood", "Vegetables"),
                difficulties = listOf("Easy", "Medium", "Hard"),
                selectedProteins = selectedProteins,
                selectedDifficulties = selectedDifficulties,
                onToggleProtein = { viewModel.toggleProtein(it) },
                onToggleDifficulty = { viewModel.toggleDifficulty(it) },
                onReset = { viewModel.resetFilters() },
                onApply = {
                    viewModel.applyFilters()
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { showFilterSheet = false }
                },
                onClose = { showFilterSheet = false }
            )
        }
    }
}