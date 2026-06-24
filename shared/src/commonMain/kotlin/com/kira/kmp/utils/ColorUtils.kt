package com.kira.kmp.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.kira.kmp.model.enums.Protein

class ColorUtils {
    // Beef: Soft Blush to Rose
    val beefGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFF1F1), Color(0xFFFDE2E2)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Pork: Pale Pink to Lavender Mist
    val porkGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFF5F8), Color(0xFFFCE7F3)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Chicken: Creamy Custard to Pale Lemon
    val chickenGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Seafood: Ice Blue to Sky Tint
    val seafoodGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF0F9FF), Color(0xFFE0F2FE)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Vegetables: Mint Cream to Soft Sage
    val vegetablesGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF0FDF4), Color(0xFFDCFCE7)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    fun getColorGradientBrush(protein: String): Brush {
        return when (protein) {
            Protein.BEEF.name -> beefGradient
            Protein.PORK.name -> porkGradient
            Protein.CHICKEN.name -> chickenGradient
            Protein.SEAFOOD.name -> seafoodGradient
            Protein.VEGETABLES.name -> vegetablesGradient
            else -> beefGradient
        }
    }

    fun getMainHeaderColor(protein: String): Color {
        return when (protein) {
            Protein.BEEF.name -> Color(0xFF5D1B1B) // Deep Wine Red
            Protein.PORK.name -> Color(0xFF5B1D3A) // Dark Plum
            Protein.CHICKEN.name -> Color(0xFF54410B) // Dark Umber / Brown
            Protein.SEAFOOD.name -> Color(0xFF0E344D) // Midnight Navy
            Protein.VEGETABLES.name -> Color(0xFF143D27) // Deep Forest Green
            else -> Color(0xFF4B4B4B)
        }
    }

    fun getSubHeaderColor(protein: String): Color {
        return when (protein) {
            Protein.BEEF.name -> Color(0xFFB34E4E) // Muted Rose
            Protein.PORK.name -> Color(0xFF9D4C73) // Muted Mauve
            Protein.CHICKEN.name -> Color(0xFF96781D) // Dark Gold/Ochre
            Protein.SEAFOOD.name -> Color(0xFF2E6A8F) // Slate Blue
            Protein.VEGETABLES.name -> Color(0xFF458B64) // Sage Green
            else -> Color(0xFF4B4B4B) // Dark Gray fallback
        }
    }

    fun getContentColor(protein: String): Color {
        return when (protein) {
            Protein.BEEF.name -> Color(0xFF2D2424) // Deep Espresso
            Protein.PORK.name -> Color(0xFF2D2428) // Dark Raisin
            Protein.CHICKEN.name -> Color(0xFF2D2B22) // Dark Olive Drab
            Protein.SEAFOOD.name -> Color(0xFF22282D) // Deep Slate
            Protein.VEGETABLES.name -> Color(0xFF222D26) // Deep Juniper
            else -> Color(0xFF000000)
        }
    }

    fun getDividerColor(protein: String): Color {
        return when (protein) {
            Protein.BEEF.name -> Color(0xFF5D1B1B).copy(alpha = 0.15f) // Subtle Wine
            Protein.PORK.name -> Color(0xFF5B1D3A).copy(alpha = 0.15f) // Subtle Plum
            Protein.CHICKEN.name -> Color(0xFF54410B).copy(alpha = 0.15f) // Subtle Bronze
            Protein.SEAFOOD.name -> Color(0xFF0E344D).copy(alpha = 0.15f) // Subtle Navy
            Protein.VEGETABLES.name -> Color(0xFF143D27).copy(alpha = 0.15f) // Subtle Forest
            else -> Color.Black.copy(alpha = 0.1f)
        }
    }

    val pastelBlue = Color(0xFFD6EEFF)
    val pastelPurple = Color(0xFFE2D7F7)
    val pastelMint = Color(0xFFE2F1E7)

    val recipeListBackgroundGradient = Brush.verticalGradient(
        colors = listOf(pastelBlue, pastelPurple, pastelMint)
    )

    val bottomNavGradient = Brush.verticalGradient(
        colors = listOf(pastelPurple, pastelMint) // Blends from the middle-bottom color to the end
    )
}
