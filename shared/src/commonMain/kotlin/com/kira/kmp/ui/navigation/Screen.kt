package com.kira.kmp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data object RecipeListRoute

@Serializable
data object FavoritesRoute

@Serializable
data object ProfileRoute

@Serializable
data class DetailScreenNavigation(val id: String)
