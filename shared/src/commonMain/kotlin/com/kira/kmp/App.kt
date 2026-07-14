package com.kira.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.ui.component.FloatingBottomNavigation
import com.kira.kmp.ui.navigation.AppNavHost
import com.kira.kmp.ui.navigation.BottomMenuItem
import com.kira.kmp.ui.navigation.DetailScreenRoute
import com.kira.kmp.ui.navigation.FavoritesRoute
import com.kira.kmp.ui.navigation.ForgotPasswordRoute
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.ProfileRoute
import com.kira.kmp.ui.navigation.RegisterRoute
import com.kira.kmp.ui.theme.FilipinoRecipeTheme
import com.kira.kmp.utils.ColorUtils
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
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
    val isDetailScreen = currentDestination?.hasRoute<DetailScreenRoute>() == true
    val isAuthScreen = currentDestination?.hasRoute<LoginRoute>() == true ||
            currentDestination?.hasRoute<RegisterRoute>() == true ||
            currentDestination?.hasRoute<ForgotPasswordRoute>() == true
    val shouldShowBottomBar = !isDetailScreen && !isAuthScreen
    val snackbarHostState = remember { SnackbarHostState() }
    val routeName = currentDestination?.route?.split(".")?.lastOrNull() // Logic to get current name
    val shouldShow = !(currentDestination?.hasRoute<DetailScreenRoute>() == true ||
            currentDestination?.hasRoute<LoginRoute>() == true ||
            currentDestination?.hasRoute<RegisterRoute>() == true)
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
                        navController,
                        viewModel,
                        snackbarHostState,
                        currentDestination?.route,
                        {})
                }
            }
        }
    }

    /*Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                val density = LocalDensity.current
                val translationY by animateFloatAsState(
                    targetValue = if (isBottomBarVisibleByScroll) 0f else with(density) { 100.dp.toPx() },
                    animationSpec = tween(durationMillis = 250),
                    label = "BottomNavTransition"
                )
                BottomNavigation(
                    modifier = Modifier.graphicsLayer { this.translationY = translationY },
                    navController = navController,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    onReselectActiveTab = {
                        isBottomBarVisibleByScroll = true
                    }
                )
            }
        },
    ) { contentPadding ->
        AppNavHost(
            mainViewModel = viewModel,
            navController = navController,
            contentPadding = contentPadding,
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
    }*/
}

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    onReselectActiveTab: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screens = listOf(
        BottomMenuItem.Recipes,
        BottomMenuItem.Favorites,
        BottomMenuItem.Profile
    )

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = modifier.background(ColorUtils().bottomNavGradient),
        windowInsets = WindowInsets.navigationBars
    ) {
        screens.forEach { bottomMenuItem ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(bottomMenuItem.route::class)
            } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val restrictedRoutes = listOf(
                        FavoritesRoute::class,
                        ProfileRoute::class
                    )
                    val isRestricted = restrictedRoutes.any { it == bottomMenuItem.route::class }
                    val isLoggedIn = viewModel.isLoggedIn()
                    if (isRestricted && !isLoggedIn) {
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Sign in to access this feature",
                                actionLabel = "Sign In",
                                duration = SnackbarDuration.Short
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                navController.navigate(LoginRoute)
                            }
                        }
                    } else if (!isSelected) {
                        navController.navigate(bottomMenuItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        onReselectActiveTab()
                        viewModel.triggerScrollToTop(bottomMenuItem.label)
                    }
                },
                icon = {
                    Icon(
                        imageVector = vectorResource(bottomMenuItem.icon),
                        contentDescription = bottomMenuItem.label,
                        modifier = Modifier.size(if (isSelected) 26.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        text = bottomMenuItem.label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
        }
    }
}
