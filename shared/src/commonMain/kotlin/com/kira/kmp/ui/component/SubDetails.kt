package com.kira.kmp.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.enums.Protein

@Composable
fun SubDetails(recipe: Recipe, modifier: Modifier = Modifier) {
    val beef = 0xFF7B1F1F
    val pork = 0xFFFFDBBB
    val chicken = 0xFFFFF9A3
    val seafood = 0xFF21618C
    val vegetables = 0xFFB6F2D1
    Row(modifier = modifier) {
        when (recipe.protein) {
            Protein.BEEF.name -> {
                RoundedTextWithIcon(
                    text = recipe.protein.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    backgroundColor = beef,
                    icon = Icons.Default.Dining,
                    textColor = 0xFFFFFFFF
                )
            }

            Protein.PORK.name -> {
                RoundedTextWithIcon(
                    text = recipe.protein.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    backgroundColor = pork,
                    icon = Icons.Default.Dining,
                    textColor = 0xFF000000
                )
            }

            Protein.CHICKEN.name -> {
                RoundedTextWithIcon(
                    text = recipe.protein.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    backgroundColor = chicken,
                    icon = Icons.Default.Dining,
                    textColor = 0xFF000000
                )
            }

            Protein.SEAFOOD.name -> {
                RoundedTextWithIcon(
                    text = recipe.protein.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    backgroundColor = seafood,
                    icon = Icons.Default.Dining,
                    textColor = 0xFFFFFFFF
                )
            }

            Protein.VEGETABLES.name -> {
                RoundedTextWithIcon(
                    text = recipe.protein.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    backgroundColor = vegetables,
                    icon = Icons.Default.Dining,
                    textColor = 0xFF000000
                )
            }

            else -> {
                // Default
            }
        }

        Spacer(Modifier.size(5.dp))

        RoundedTextWithIcon(
            text = "${recipe.estimatedMinutes} mins",
            icon = Icons.Default.WatchLater,
            backgroundColor = 0xFFB8E986,
            textColor = 0xFF000000
        )

        Spacer(Modifier.size(5.dp))

        RoundedTextWithIcon(
            text = recipe.difficulty.lowercase().replaceFirstChar { it.uppercase() },
            icon = Icons.Filled.StackedBarChart,
            backgroundColor = 0xFFB39DDB,
            textColor = 0xFF000000
        )
    }
}
