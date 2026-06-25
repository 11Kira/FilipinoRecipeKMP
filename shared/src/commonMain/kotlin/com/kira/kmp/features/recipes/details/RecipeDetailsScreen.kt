package com.kira.kmp.features.recipes.details

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kira.kmp.model.Recipe
import com.kira.kmp.ui.component.CircularIconButton
import com.kira.kmp.ui.component.DetailsListSection
import com.kira.kmp.ui.component.SubDetails
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.ic_favorite
import filipinorecipekmp.shared.generated.resources.ic_favorite_filled
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipeDetailsScreen(
    id: String,
    onShowSnackbar: (String, String?, (() -> Unit)?) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RecipeDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.recipeDetailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.getRecipeById(id)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            onShowSnackbar(it, null, null)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        uiState.recipe?.let { recipe ->
            PopulateRecipeDetails(
                viewModel = viewModel,
                recipe = recipe,
                onBackClick = onBackClick,
                onShowSnackbar = onShowSnackbar,
                onNavigateToLogin = onNavigateToLogin
            )
        }

        if (uiState.isLoading && uiState.recipe == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun PopulateRecipeDetails(
    viewModel: RecipeDetailsViewModel,
    recipe: Recipe,
    onBackClick: () -> Unit,
    onShowSnackbar: (String, String?, (() -> Unit)?) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val scrollState = rememberScrollState()
    val headerHeight = 500.dp
    val toolbarHeight = 56.dp
    val showToolbarThreshold = with(LocalDensity.current) { (headerHeight - toolbarHeight).toPx() }
    val toolbarAlpha = (scrollState.value / showToolbarThreshold).coerceIn(0f, 1f)
    Box(modifier = Modifier.fillMaxSize()) {
        RecipeHeaderImage(
            recipe = recipe,
            headerHeight = headerHeight,
            scrollState = scrollState
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(headerHeight))
            RecipeContent(recipe)
        }

        RecipeTopBar(
            viewModel = viewModel,
            alpha = toolbarAlpha,
            recipe = recipe,
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

@Composable
fun RecipeHeaderImage(
    recipe: Recipe,
    headerHeight: Dp,
    scrollState: ScrollState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .graphicsLayer {
                translationY = -scrollState.value * 0.5f
                alpha = 1f - (scrollState.value / 1000f).coerceIn(0f, 0.7f)
            }
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )
        val density = LocalDensity.current
        val startYPx = with(density) { (headerHeight * 0.6f).toPx() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = startYPx
                    )
                )
        )

        SubDetails(
            recipe = recipe,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun RecipeTopBar(
    viewModel: RecipeDetailsViewModel,
    alpha: Float,
    recipe: Recipe,
    onBackClick: () -> Unit,
    onShowSnackbar: (String, String?, (() -> Unit)?) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = alpha))
    ) {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircularIconButton(icon = Icons.Default.ArrowBack, onClick = onBackClick)

            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = alpha),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
            CircularIconButton(
                icon = vectorResource(if (recipe.isFavorited) Res.drawable.ic_favorite_filled else Res.drawable.ic_favorite),
                tint = if (recipe.isFavorited) Color.Red else Color.White,
                onClick = {
                    if (isLoggedIn) {
                        viewModel.toggleFavoriteRecipe(recipe.id)
                    } else {
                        onShowSnackbar(
                            "Sign in to favorite this recipe",
                            "Sign In",
                            onNavigateToLogin
                        )
                    }
                },
                isFlipped = true
            )
        }
    }
}

@Composable
fun IngredientsSection(recipe: Recipe) {
    Text(
        textAlign = TextAlign.Start,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.wrapContentWidth(),
        text = "Ingredients:",
        color = ColorUtils().getMainHeaderColor(recipe.protein),
    )
    if (recipe.ingredients.main.isNotEmpty()) {
        DetailsListSection(
            text = "Main:",
            isAnchorHeader = false,
            protein = recipe.protein,
            list = recipe.ingredients.main
        )
    }
    if (recipe.ingredients.aromatics.isNotEmpty()) {
        DetailsListSection(
            text = "Aromatics:",
            isAnchorHeader = false,
            protein = recipe.protein,
            list = recipe.ingredients.aromatics
        )
    }
    if (recipe.ingredients.liquidsAndSeasonings.isNotEmpty()) {
        DetailsListSection(
            text = "Liquids and Seasonings:",
            isAnchorHeader = false,
            protein = recipe.protein,
            list = recipe.ingredients.liquidsAndSeasonings
        )
    }
    if (recipe.ingredients.vegetables.isNotEmpty()) {
        DetailsListSection(
            text = "Vegetables:",
            isAnchorHeader = false,
            protein = recipe.protein,
            list = recipe.ingredients.vegetables
        )
    }
    if (recipe.ingredients.optionalAddons.isNotEmpty()) {
        DetailsListSection(
            text = "Optional Add-ons:",
            isAnchorHeader = false,
            protein = recipe.protein,
            list = recipe.ingredients.optionalAddons
        )
    }
}

@Composable
fun RecipeContent(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = ColorUtils().getColorGradientBrush(recipe.protein))
            .padding(16.dp)
    ) {
        Text(
            text = recipe.title,
            fontSize = 32.sp,
            color = ColorUtils().getMainHeaderColor(recipe.protein),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = recipe.description,
            style = TextStyle(
                lineHeight = 20.sp,
                color = ColorUtils().getContentColor(recipe.protein)
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = ColorUtils().getDividerColor(protein = recipe.protein)
        )
        IngredientsSection(recipe = recipe)
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = ColorUtils().getDividerColor(protein = recipe.protein)
        )
        DetailsListSection(
            text = "Steps:",
            isAnchorHeader = true,
            protein = recipe.protein,
            list = recipe.steps
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = ColorUtils().getDividerColor(protein = recipe.protein)
        )
        DetailsListSection(
            text = "Cooking Tips:",
            isAnchorHeader = true,
            protein = recipe.protein,
            list = recipe.cookingTips
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = ColorUtils().getDividerColor(protein = recipe.protein)
        )
        DetailsListSection(
            text = "Variations:",
            isAnchorHeader = true,
            protein = recipe.protein,
            list = recipe.variations
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = ColorUtils().getDividerColor(protein = recipe.protein)
        )
        DetailsListSection(
            text = "Serving Suggestions:",
            isAnchorHeader = true,
            protein = recipe.protein,
            list = recipe.servingSuggestions
        )
    }
}
