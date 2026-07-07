package com.kira.kmp.ui.component.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kira.kmp.model.Recipe
import com.kira.kmp.utils.ColorUtils

@Composable
fun RecipeBaseScreen(
    recipes: LazyPagingItems<Recipe>,
    query: String,
    onQueryChange: (String) -> Unit,
    onItemClick: (String) -> Unit,
    contentPadding: PaddingValues,
    searchHint: String,
    actionSlot: @Composable (RowScope.() -> Unit)? = null,
) {

    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    var lastScrolledQuery by rememberSaveable { mutableStateOf("") }
    val refreshState = recipes.loadState.refresh

    // Auto-scroll to top when query changes
    LaunchedEffect(refreshState) {
        if (refreshState is LoadState.NotLoading && recipes.itemCount > 0) {
            if (query != lastScrolledQuery) {
                listState.scrollToItem(0)
                lastScrolledQuery = query
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorUtils().recipeListBackgroundGradient)
    ) {
        if (refreshState is LoadState.Error && recipes.itemCount == 0) {
            ErrorScreen(
                message = "Connection error... try again!",
                onRetry = { recipes.retry() }
            )
        } else {
            RecipeList(
                recipes = recipes,
                listState = listState,
                searchQuery = query,
                contentPadding = contentPadding,
                onItemClick = onItemClick
            )
        }

        // Status bar scrim
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )

        // Top Header (Search + Optional Filter)
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
                textHint = searchHint,
                onValueChange = onQueryChange,
                onClear = { onQueryChange("") },
                focusManager = focusManager,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp)),
            )

            // Inject the Filter button here if provided
            actionSlot?.invoke(this)
        }
    }
}