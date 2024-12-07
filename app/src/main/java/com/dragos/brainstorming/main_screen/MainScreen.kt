package com.dragos.brainstorming.main_screen

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dragos.brainstorming.MainApplication
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val goodAppList by viewModel.goodAppList.collectAsStateWithLifecycle()
    val badAppList by viewModel.badAppList.collectAsStateWithLifecycle()
    val appList by viewModel.appList.collectAsStateWithLifecycle()

    MainScreen(
        modifier = modifier,
        goodAppList = goodAppList,
        badAppList = badAppList,
        appList = appList,
        goodAppClick = viewModel::goodAppClick,
        badAppClick = viewModel::badAppClick,
        removeClick = viewModel::removeAppClick
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    goodAppList: List<AppInfo>,
    badAppList: List<AppInfo>,
    appList: List<AppInfo>,
    goodAppClick: (AppInfo) -> Unit,
    badAppClick: (AppInfo) -> Unit,
    removeClick: (AppInfo) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(
                "Good Apps: ${goodAppList.size}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(goodAppList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${it.appName}: ${it.appTime / 1000 / 60}")
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        removeClick(it)
                    }
                ) {
                    Icon(Icons.Default.Lock, "lock")
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
        }

        item {
            Text(
                "Bad App List: ${badAppList.size}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

        }

        items(badAppList) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${it.appName}: ${it.appTime / 1000 / 60}")

                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            removeClick(it)
                        }
                    ) {
                        Icon(painterResource(R.drawable.thumb_down_24px), "thumbs down")
                    }
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .fillMaxSize(),
                        painter = rememberDrawablePainter(MainApplication.packageManager.getApplicationIcon(it.packageName)),
                        contentDescription = ""
                    )
                }
                HorizontalDivider()
            }

        }

        item {
            Text(
                "All Apps: ${appList.size}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(appList) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${it.appName}: ${it.appTime / 1000 / 60}")
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            goodAppClick(it)
                        }
                    ) {
                        Icon(Icons.Default.ThumbUp, "add")
                    }
                    IconButton(
                        onClick = {
                            badAppClick(it)
                        }
                    ) {
                        Icon(painterResource(R.drawable.thumb_down_24px), "thumbs down")
                    }
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .fillMaxSize(),
                        painter = rememberDrawablePainter(MainApplication.packageManager.getApplicationIcon(it.packageName)),
                        contentDescription = ""
                    )

                }
                HorizontalDivider()
            }
        }
    }
}




