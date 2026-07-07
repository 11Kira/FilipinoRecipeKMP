package com.kira.kmp.features.recipes.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.kmp.ui.component.recipe.RecipeBaseScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteRecipeListScreen(
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: FavoriteRecipeListViewModel = koinViewModel(),
) {
    val recipes = viewModel.favoritePagingFlow.collectAsLazyPagingItems()
    val query by viewModel.searchQuery.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(recipes.loadState.append) {
        val append = recipes.loadState.append
        if (append is LoadState.Error) {
            onShowSnackbar("Failed to load more recipes")
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

    RecipeBaseScreen(
        recipes = recipes,
        query = query,
        onQueryChange = { viewModel.onSearchQueryChanged(it) },
        onItemClick = onItemClick,
        contentPadding = contentPadding,
        searchHint = "Search favorites..."
    )
}
