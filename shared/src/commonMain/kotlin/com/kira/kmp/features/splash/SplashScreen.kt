package com.kira.kmp.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.RecipeListRoute
import com.kira.kmp.ui.navigation.SplashRoute
import com.kira.kmp.utils.ColorUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.checkAuthStatus()
    }

    LaunchedEffect(Unit) {
        viewModel.startDestination.collect { state ->
            val route = when (state) {
                is StartDestination.Home -> RecipeListRoute
                is StartDestination.Login -> LoginRoute
            }

            navController.navigate(route) {
                popUpTo(SplashRoute) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = ColorUtils().recipeListBackgroundGradient)
    )
}
