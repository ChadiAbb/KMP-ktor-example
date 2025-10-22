package org.company.app.utilsUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextAlign


class ExpandableList<T> (
    val groups: List<Group>,
    val elements: Map<String, List<T>>
) {
    data class Group (val title : String)
}

fun createGroups(titles: List<String>): List<ExpandableList.Group> {
    return titles.map { ExpandableList.Group(it) }
}

@Composable
fun <T> ExpandableListPreview(
    expandableList: ExpandableList<T>,
    displayItem : @Composable (T) -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(expandableList.groups) { group ->
            ExpandableGroup(
                group = group,
                textColor = textColor,
                items = expandableList.elements[group.title] ?: emptyList(),
                displayItem = displayItem
            )
        }
    }
}

@Composable
private fun <T> ExpandableGroup(
    group: ExpandableList.Group,
    textColor: Color,
    items: List<T>,
    displayItem : @Composable (T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Row(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isExpanded) "▼" else "▶",
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = group.title,
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (isExpanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            ) {
                items.forEach { item ->
                    displayItem(item)
                }
            }
        }
    }
}

