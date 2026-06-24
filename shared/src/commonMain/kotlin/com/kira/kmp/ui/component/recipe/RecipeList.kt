package com.kira.kmp.ui.component.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import com.kira.kmp.model.Recipe

@Composable
fun RecipeList(
    recipes: LazyPagingItems<Recipe>,
    listState: LazyListState,
    searchQuery: String,
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit
) {
    val refreshState = recipes.loadState.refresh
    val shimmerBrush = RecipeShimmerBrush()
    
    Box(modifier = Modifier.fillMaxSize()) {
        if (refreshState is LoadState.Error) {
            Text(
                text = "Error: ${refreshState.error.message}",
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 120.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = contentPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            if (refreshState is LoadState.Loading) {
                items(5) {
                    RecipeShimmerItem(shimmerBrush = shimmerBrush)
                }
            } else {
                items(
                    count = recipes.itemCount,
                    key = { index ->
                        val recipe = recipes[index]
                        "${recipe?.id}_$searchQuery"
                    },
                    contentType = recipes.itemContentType { "recipe_item" }
                ) { index ->
                    val recipe = recipes[index]
                    recipe?.let { selectedRecipe ->
                        RecipeCardItem(
                            selectedRecipe = selectedRecipe,
                            onItemClick = onItemClick,
                        )
                    }
                }
            }
        }
    }
}
