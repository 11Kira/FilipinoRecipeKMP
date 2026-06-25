package com.kira.kmp.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kira.kmp.ui.navigation.RecipeListRoute
import com.kira.kmp.ui.navigation.SplashRoute
import com.kira.kmp.utils.ColorUtils
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(1500)
        navController.navigate(RecipeListRoute) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = ColorUtils().recipeListBackgroundGradient)
    )
}
