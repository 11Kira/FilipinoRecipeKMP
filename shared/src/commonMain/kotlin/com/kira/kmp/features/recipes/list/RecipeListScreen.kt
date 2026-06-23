package com.kira.kmp.features.recipes.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.kmp.ui.component.recipe.RecipeFilter
import com.kira.kmp.ui.component.recipe.RecipeFilterChip
import com.kira.kmp.ui.component.recipe.RecipeList
import com.kira.kmp.ui.component.recipe.RecipeSearchField
import com.kira.kmp.utils.ColorUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeListScreen(
    contentPadding: PaddingValues,
    onItemClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: RecipeListViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue -> newValue != SheetValue.Hidden })
    val recipes = viewModel.recipePagingFlow.collectAsLazyPagingItems()
    val focusManager = LocalFocusManager.current
    val query by viewModel.searchQuery.collectAsState()
    var lastScrolledQuery by rememberSaveable { mutableStateOf("") }
    val selectedProteins by viewModel.selectedProteins.collectAsState()
    val selectedDifficulties by viewModel.selectedDifficulties.collectAsState()
    val appliedFilterCount by viewModel.appliedFilterCount.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

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
            onItemClick = onItemClick
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
                textHint = "Search recipes...",
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                onClear = { viewModel.onSearchQueryChanged("") },
                focusManager = focusManager,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp)),
            )

            RecipeFilter(
                onButtonClick = {
                    focusManager.clearFocus()
                    viewModel.syncSelectedWithApplied()
                    scope.launch {
                        delay(100)
                        showFilterSheet = true
                        sheetState.show()
                    }
                },
                appliedFilterCount = appliedFilterCount
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(370.dp)
                        .padding(24.dp)
                        .navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter Recipes",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                        IconButton(onClick = { showFilterSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    val proteins = listOf("Pork", "Beef", "Chicken", "Seafood", "Vegetables")
                    val difficulties = listOf("Easy", "Medium", "Hard")

                    Text(
                        "Protein",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        proteins.forEach { protein ->
                            RecipeFilterChip(
                                label = protein,
                                isSelected = selectedProteins.contains(protein),
                                onToggle = { viewModel.toggleProtein(protein) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Difficulty",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        difficulties.forEach { level ->
                            RecipeFilterChip(
                                label = level,
                                isSelected = selectedDifficulties.contains(level),
                                onToggle = { viewModel.toggleDifficulty(level) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.resetFilters() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Reset") }

                        Button(
                            onClick = {
                                viewModel.applyFilters()
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    showFilterSheet = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) { Text("Apply") }
                    }
                }
            }
        }
    }
}
