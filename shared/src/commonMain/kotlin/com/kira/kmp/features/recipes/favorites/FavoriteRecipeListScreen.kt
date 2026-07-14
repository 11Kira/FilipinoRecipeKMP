package com.kira.kmp.features.recipes.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.ui.component.recipe.RecipeBaseScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteRecipeListScreen(
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit,
    viewModel: FavoriteRecipeListViewModel = koinViewModel(),
    mainViewModel: MainViewModel,
) {
    val listState = rememberLazyListState()
    val recipes = viewModel.favoritePagingFlow.collectAsLazyPagingItems()
    val refreshState = recipes.loadState.refresh
    var isManualRefresh by remember { mutableStateOf(false) }
    val isRefreshing = refreshState is LoadState.Loading && isManualRefresh
    val query by viewModel.searchQuery.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { offset ->
                if (offset == 0) mainViewModel.isBottomNavExpanded = true
            }
    }

    LaunchedEffect(refreshState) {
        if (refreshState is LoadState.NotLoading || refreshState is LoadState.Error) {
            isManualRefresh = false
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                recipes.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
            screenLabel = "Favorites",
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            onItemClick = onItemClick,
            contentPadding = contentPadding,
            searchHint = "Search favorites..."
        )
    }
}
