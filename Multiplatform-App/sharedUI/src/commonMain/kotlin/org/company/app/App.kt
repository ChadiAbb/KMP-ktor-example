package org.company.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import multiplatform_app.sharedui.generated.resources.Res
import multiplatform_app.sharedui.generated.resources.ic_dark_mode
import multiplatform_app.sharedui.generated.resources.ic_light_mode
import multiplatform_app.sharedui.generated.resources.open_github
import multiplatform_app.sharedui.generated.resources.theme
import org.company.app.network.Room
import org.company.app.network.RoomViewModel
import org.company.app.theme.AppTheme
import org.company.app.theme.LocalThemeIsDark
import org.company.app.utilsUI.ExpandableListPreview
import org.company.app.utilsUI.ToolsBarPreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) {
    val viewModel = remember { RoomViewModel() }
    LaunchedEffect(Unit) {
        viewModel.fetchRooms()
    }

    AppTheme(onThemeChanged) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Free Univ",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        var isDark by LocalThemeIsDark.current
                        val icon = remember(isDark) {
                            if (isDark) Res.drawable.ic_light_mode
                            else Res.drawable.ic_dark_mode
                        }
                        IconButton(
                            onClick = { isDark = !isDark }
                        ) {
                            Icon(
                                vectorResource(icon),
                                contentDescription = stringResource(Res.string.theme)
                            )
                        }
                    }
                )
            },
            content = { contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    val uriHandler = LocalUriHandler.current
                    TextButton(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .align(Alignment.End),
                        onClick = { uriHandler.openUri("https://github.com/terrakok") },
                    ) {
                        Text(stringResource(Res.string.open_github))
                    }

                    var decreasing by remember { mutableStateOf(false) }
                    ToolsBarPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        decreasing = decreasing,
                        onDecreasingChange = {
                            decreasing = it
                            viewModel.updateDecreasingOrder(it)
                        },
                        refresh = { viewModel.fetchRooms() }
                    )

                    when (val list = viewModel.expandableList.value) {
                        null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        else -> {
                            ExpandableListPreview(
                                expandableList = list,
                                displayItem = { room: Room ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp, horizontal = 8.dp), // Ajoutez un padding horizontal pour éviter de toucher les bords
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = room.title,
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Start
                                            )
                                            Text(
                                                text = "${room.freetime} min",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.secondary,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}
