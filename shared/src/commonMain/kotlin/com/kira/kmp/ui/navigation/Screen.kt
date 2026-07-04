package com.kira.kmp.ui.navigation

import kotlinx.serialization.Serializable

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
data class DetailScreenRoute(val id: String)

@Serializable
data object ForgotPasswordRoute