package com.kira.kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.ui.component.FloatingBottomNavigation
import com.kira.kmp.ui.navigation.AppNavHost
import com.kira.kmp.ui.navigation.DetailScreenRoute
import com.kira.kmp.ui.navigation.FavoritesRoute
import com.kira.kmp.ui.navigation.ForgotPasswordRoute
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.ProfileRoute
import com.kira.kmp.ui.navigation.RecipeListRoute
import com.kira.kmp.ui.navigation.RegisterRoute
import com.kira.kmp.ui.theme.FilipinoRecipeTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MainViewModel = koinViewModel()) {
    FilipinoRecipeTheme {
        MainScreenView(viewModel)
    }
}

@Composable
fun MainScreenView(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentTabLabel = when {
        currentDestination?.hasRoute<RecipeListRoute>() == true -> "Recipes"
        currentDestination?.hasRoute<FavoritesRoute>() == true -> "Favorites"
        currentDestination?.hasRoute<ProfileRoute>() == true -> "Profile"
        else -> "Recipes"
    }
    val isDetailScreen = currentDestination?.hasRoute<DetailScreenRoute>() == true
    val isAuthScreen = currentDestination?.hasRoute<LoginRoute>() == true ||
            currentDestination?.hasRoute<RegisterRoute>() == true ||
            currentDestination?.hasRoute<ForgotPasswordRoute>() == true
    val snackbarHostState = remember { SnackbarHostState() }
    val shouldShow = !isDetailScreen && !isAuthScreen
    val scope = rememberCoroutineScope()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -10f) viewModel.isBottomNavExpanded = false
                else if (available.y > 10f) viewModel.isBottomNavExpanded = true
                return Offset.Zero
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AppNavHost(
                viewModel,
                navController,
                contentPadding,
                onShowSnackbar = { message, actionLabel, action ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = actionLabel,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) action?.invoke()
                    }
                }
            )

            if (shouldShow) {
                Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)) {
                    FloatingBottomNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        currentDestination = currentTabLabel,
                        {})
                }
            }
        }
    }
}