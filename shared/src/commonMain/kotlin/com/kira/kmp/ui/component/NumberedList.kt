package com.kira.kmp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.kmp.utils.ColorUtils

@Composable
fun NumberedList(
    items: List<String>,
    protein: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${index + 1}.",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = ColorUtils().getContentColor(protein),
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = item,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    color = ColorUtils().getContentColor(protein),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
