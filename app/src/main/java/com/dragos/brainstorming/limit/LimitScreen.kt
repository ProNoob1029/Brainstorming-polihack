package com.dragos.brainstorming.limit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dragos.brainstorming.AppInfo
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun LimitScreen(
    modifier: Modifier = Modifier,
    viewModel: LimitViewModel = viewModel()
) {
    val limitApps by viewModel.limitApps.collectAsStateWithLifecycle()
    val remainingApps by viewModel.remainingApps.collectAsStateWithLifecycle()


    LimitScreen(
        modifier = modifier,
        limitApps = limitApps,
        remainingApps = remainingApps,
        addLimitApp = viewModel::addLimitApp,
        removeLimitApp = viewModel::removeLimitApp,
        addMinutes = viewModel::addMinutes,
        removeMinutes = viewModel::removeMinutes
    )
}


@Composable
fun LimitScreen(
    modifier: Modifier = Modifier,
    limitApps: List<AppInfo>,
    addLimitApp: (AppInfo) -> Unit,
    removeLimitApp: (AppInfo) -> Unit,
    remainingApps: List<AppInfo>,
    addMinutes: (AppInfo) -> Unit,
    removeMinutes: (AppInfo) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                "Apps to set limit: ${limitApps.size}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(limitApps)
        {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${it.appName} limit: ${it.timeLimit}",
                        modifier = Modifier.weight(1f)
                    )

                    FilledIconButton(
                        onClick = {
                            removeMinutes(it)
                        }
                    ) {
                        Icon(painterResource(R.drawable.remove_24px), "remove")
                    }

                    FilledIconButton(
                        onClick = {
                            addMinutes(it)
                        }
                    ) {
                        Icon(Icons.Default.Add, "add")
                    }

                    IconButton(
                        onClick = {
                            removeLimitApp(it)
                        }
                    ) {
                        Icon(Icons.Default.Clear, "clear")
                    }
                    Image(
                        modifier = Modifier
                            .size(60.dp),
                        painter = rememberDrawablePainter(
                            MainApplication.packageManager.getApplicationIcon(
                                it.packageName
                            )
                        ),
                        contentDescription = ""
                    )

                }
                HorizontalDivider()
            }
        }

        item {
            Text(
                "Total apps:",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(remainingApps)
        {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${it.appName}: ${it.appTime / 1000 / 60} minutes today")
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            addLimitApp(it)
                        }
                    ) {
                        Icon(Icons.Default.ThumbUp, "add")
                    }
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .fillMaxSize(),
                        painter = rememberDrawablePainter(
                            MainApplication.packageManager.getApplicationIcon(
                                it.packageName
                            )
                        ),
                        contentDescription = ""
                    )

                }
                HorizontalDivider()
            }
        }
    }
}