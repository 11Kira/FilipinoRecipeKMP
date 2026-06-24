package com.kira.kmp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.kmp.utils.ColorUtils

@Composable
fun DetailsListSection(text: String, isAnchorHeader: Boolean, protein: String, list: List<String>) {
    Text(
        textAlign = TextAlign.Start,
        fontSize = if (isAnchorHeader) 22.sp else 16.sp,
        fontFamily = FontFamily.Default,
        fontWeight = if (isAnchorHeader) FontWeight.Bold else FontWeight.Medium,
        modifier = Modifier
            .wrapContentWidth()
            .padding(top = 16.dp),
        text = text,
        color = if (isAnchorHeader) ColorUtils().getMainHeaderColor(protein) else ColorUtils().getSubHeaderColor(
            protein
        )
    )
    NumberedList(list, protein)
}
