package com.kira.kmp.ui.component.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.ic_filter
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeFilter(
    onButtonClick: () -> Unit,
    appliedFilterCount: Int
) {
    Surface(
        modifier = Modifier
            .size(50.dp)
            .shadow(elevation = 4.dp, shape = CircleShape),
        shape = CircleShape,
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { onButtonClick.invoke() }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(24.dp),
                )
            }

            if (appliedFilterCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 6.dp, end = 6.dp),
                    containerColor = Color.Black,
                    contentColor = Color.White
                ) {
                    Text(text = appliedFilterCount.toString())
                }
            }
        }
    }
}
