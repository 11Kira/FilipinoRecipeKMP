package com.kira.kmp.ui.navigation

import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.ic_account
import filipinorecipekmp.shared.generated.resources.ic_chef
import filipinorecipekmp.shared.generated.resources.ic_heart
import org.jetbrains.compose.resources.DrawableResource

sealed class BottomMenuItem(val label: String, val icon: DrawableResource, val route: Any) {
    data object Recipes : BottomMenuItem("Recipes", Res.drawable.ic_chef, RecipeListRoute)
    data object Favorites : BottomMenuItem("Favorites", Res.drawable.ic_heart, FavoritesRoute)
    data object Profile : BottomMenuItem("Profile", Res.drawable.ic_account, ProfileRoute)
}
