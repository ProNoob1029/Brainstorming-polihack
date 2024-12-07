package com.dragos.brainstorming

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    goodAppList: List<AppInfo>,
    badAppList: List<AppInfo>,
    appList: List<AppInfo>,
    goodAppClick: (AppInfo) -> Unit,
    badAppClick: (AppInfo) -> Unit,
    removeClick: (AppInfo) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("Good Apps")
        }

        items(goodAppList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${it.appName}: ${it.appTime}")
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
                    painter = rememberDrawablePainter(it.appIcon),
                    contentDescription = ""
                )
            }
        }

        item {
            Text("Bad App List")
        }

        items(badAppList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${it.appName}: ${it.appTime}")
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
                    painter = rememberDrawablePainter(it.appIcon),
                    contentDescription = ""
                )
            }
        }

        item {
            Text("All Apps")
        }

        items(appList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${it.appName}: ${it.appTime}")
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
                    Icon(Icons.Default.Lock, "lock")
                }
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .fillMaxSize(),
                    painter = rememberDrawablePainter(it.appIcon),
                    contentDescription = ""
                )
            }
        }
    }
}