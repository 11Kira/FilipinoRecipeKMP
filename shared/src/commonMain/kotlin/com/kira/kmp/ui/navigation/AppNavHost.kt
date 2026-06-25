package com.kira.kmp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kira.kmp.features.account.auth.login.LoginScreen
import com.kira.kmp.features.account.auth.register.RegisterScreen
import com.kira.kmp.features.account.profile.ProfileScreen
import com.kira.kmp.features.recipes.details.RecipeDetailsScreen
import com.kira.kmp.features.recipes.favorites.FavoriteRecipeListScreen
import com.kira.kmp.features.recipes.list.RecipeListScreen
import com.kira.kmp.features.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    onShowSnackbar: (String, String?, (() -> Unit)?) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        composable<SplashRoute> { SplashScreen(navController) }
        composable<LoginRoute> {
            LoginScreen(
                navController = navController,
                onShowSnackbar = { msg -> onShowSnackbar(msg, null, null) }
            )
        }
        composable<RegisterRoute> {
            RegisterScreen(
                navController = navController,
                onShowSnackbar = { msg -> onShowSnackbar(msg, null, null) }
            )
        }
        composable<RecipeListRoute> {
            RecipeListScreen(
                contentPadding = contentPadding,
                onItemClick = { id ->
                    navController.navigate(DetailScreenNavigation(id))
                },
                onShowSnackbar = { msg -> onShowSnackbar(msg, null, null) }
            )
        }

        composable<FavoritesRoute> {
            FavoriteRecipeListScreen(
                contentPadding = contentPadding,
                onItemClick = { id ->
                    navController.navigate(DetailScreenNavigation(id))
                },
                onShowSnackbar = { msg -> onShowSnackbar(msg, null, null) }
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(
                onShowSnackbar = { msg -> onShowSnackbar(msg, null, null) },
                onLogoutNavigate = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<DetailScreenNavigation> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailScreenNavigation>()
            RecipeDetailsScreen(
                id = args.id,
                onShowSnackbar = onShowSnackbar,
                onBackClick = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate(LoginRoute)
                }
            )
        }
    }
}
