package com.kira.kmp.features.recipes.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.kmp.model.Recipe
import com.kira.kmp.ui.component.recipe.RecipeList
import com.kira.kmp.ui.component.recipe.RecipeSearchField
import com.kira.kmp.utils.ColorUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteRecipeListScreen(
    viewModel: FavoriteRecipeListViewModel = koinViewModel(),
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val recipes = viewModel.favoritePagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(recipes.loadState) {
        val refresh = recipes.loadState.refresh
        if (refresh is LoadState.Error) {
            println("FavoriteRecipeListScreen Error: ${refresh.error.message}")
            onShowSnackbar("Error: ${refresh.error.message}")
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
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
    MainFavoriteRecipeScreen(
        viewModel = viewModel,
        recipes = recipes,
        contentPadding = contentPadding,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFavoriteRecipeScreen(
    viewModel: FavoriteRecipeListViewModel,
    recipes: LazyPagingItems<Recipe>,
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val query by viewModel.searchQuery.collectAsState()
    var lastScrolledQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(recipes.loadState.refresh) {
        if (recipes.loadState.refresh is LoadState.NotLoading && recipes.itemCount > 0) {
            if (query != lastScrolledQuery) {
                listState.scrollToItem(0)
                lastScrolledQuery = query
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = ColorUtils().recipeListBackgroundGradient)
    ) {
        RecipeList(
            recipes = recipes,
            listState = listState,
            searchQuery = query,
            contentPadding = contentPadding,
            onItemClick = onItemClick,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            RecipeSearchField(
                query = query,
                textHint = "Search favorites...",
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                onClear = { viewModel.onSearchQueryChanged("") },
                focusManager = focusManager,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp)),
            )
        }
    }
}
