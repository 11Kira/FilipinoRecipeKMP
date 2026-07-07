package com.kira.kmp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterSheetContent(
    proteins: List<String>,
    difficulties: List<String>,
    selectedProteins: Set<String>,
    selectedDifficulties: Set<String>,
    onToggleProtein: (String) -> Unit,
    onToggleDifficulty: (String) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(370.dp)
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filter Recipes",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = { onClose() }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Text(
            "Protein",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            proteins.forEach { protein ->
                RecipeFilterChip(
                    label = protein,
                    isSelected = selectedProteins.contains(protein),
                    onToggle = { onToggleProtein(protein) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Difficulty",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            difficulties.forEach { level ->
                RecipeFilterChip(
                    label = level,
                    isSelected = selectedDifficulties.contains(level),
                    onToggle = { onToggleDifficulty(level) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onReset() },
                modifier = Modifier.weight(1f)
            ) { Text("Reset") }

            Button(
                onClick = { onApply() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) { Text("Apply") }
        }
    }
}