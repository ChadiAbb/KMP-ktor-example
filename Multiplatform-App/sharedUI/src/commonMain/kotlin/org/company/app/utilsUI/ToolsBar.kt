package org.company.app.utilsUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ToolsBarPreview(
    modifier: Modifier = Modifier,
    decreasing: Boolean,
    onDecreasingChange: (Boolean) -> Unit,
    textColor: Color = MaterialTheme.colorScheme.primary,
    refresh: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = decreasing,
                onCheckedChange = onDecreasingChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Decreasing",
                color = textColor,
                textAlign = TextAlign.Start
            )
        }
        TextButton(
            onClick = refresh,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text("Refresh")
        }
    }
}