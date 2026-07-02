package com.kira.kmp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StaticBasicTextFieldWithIcon(
    value: String,
    imageVector: ImageVector
) {
    BasicTextField(
        value = value,
        enabled = false,
        onValueChange = {},
        modifier = Modifier.height(50.dp),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        Color.Gray.copy(alpha = 0.5f),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.Gray,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    innerTextField()
                }
            }
        }
    )
}