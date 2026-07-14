package com.kira.kmp.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.ui.navigation.BottomMenuItem
import com.kira.kmp.ui.navigation.FavoritesRoute
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.ProfileRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FloatingBottomNavigation(
    navController: NavController,
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    currentDestination: String?, // Should be the full route name or a string identifier
    onReselect: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val screens = listOf(BottomMenuItem.Recipes, BottomMenuItem.Favorites, BottomMenuItem.Profile)
    val isExpanded = viewModel.isBottomNavExpanded

    AnimatedContent(
        targetState = isExpanded,
        transitionSpec = {
            (fadeIn(tween(200)) togetherWith fadeOut(tween(200)))
                .using(SizeTransform(clip = false) { _, _ ->
                    spring(
                        Spring.DampingRatioNoBouncy,
                        Spring.StiffnessLow
                    )
                })
        },
        label = "NavMorph"
    ) { expanded ->
        Box(
            modifier = Modifier
                .height(60.dp)
                .width(if (expanded) 290.dp else 60.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(30.dp), clip = false)
                .background(Color.White.copy(alpha = 0.96f), RoundedCornerShape(30.dp))
                .clickable { if (!expanded) viewModel.isBottomNavExpanded = true },
            contentAlignment = Alignment.Center
        ) {
            if (!expanded) {
                // COLLAPSED: Single Icon
                val active =
                    screens.find { it.label == currentDestination } ?: BottomMenuItem.Recipes
                Icon(
                    vectorResource(active.icon),
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                // EXPANDED: Row with Icon + Text
                Row(
                    modifier = Modifier.width(290.dp).fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    screens.forEach { item ->
                        val isSelected = item.label == currentDestination
                        val color =
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

                        Column(
                            Modifier.weight(1f).fillMaxHeight().clickable {
                                // 1. Auth check
                                if ((item.route::class == FavoritesRoute::class || item.route::class == ProfileRoute::class) && !viewModel.isLoggedIn()) {
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
                                    return@clickable
                                }
                                // 2. Navigation or Scroll
                                if (isSelected) {
                                    onReselect()
                                    viewModel.triggerScrollToTop(item.label)
                                } else {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                vectorResource(item.icon),
                                contentDescription = item.label,
                                tint = color,
                                modifier = Modifier.size(22.dp)
                            )
                            // LABEL: Only visible when expanded
                            Text(
                                text = item.label,
                                color = color,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}