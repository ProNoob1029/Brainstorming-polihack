package com.dragos.brainstorming.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun MyLimit(
    modifier: Modifier = Modifier,
    viewModel: LimitViewModel = viewModel()
) {
    val setlimit by viewModel.setLimit.collectAsStateWithLifecycle()
    val applist by viewModel.appList.collectAsStateWithLifecycle()


    MyLimit(
        modifier = modifier,
        setLimit = setlimit,
        setLim = viewModel::setLim,
        delLim = viewModel::delLim,
        fullAppList = applist
    )
}


@Composable
fun MyLimit(
    modifier: Modifier = Modifier,
    setLimit: List<AppInfo>,
    setLim: (AppInfo) -> Unit,
    delLim: (AppInfo) -> Unit,
    fullAppList: List<AppInfo>,

) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                "Apps to set limit: ${setLimit.size}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(setLimit)
        {
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
                            delLim(it)
                        }
                    ) {
                        Icon(Icons.Default.Clear, "clear")
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

        item {
            Text(
                "Total apps:",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        items(fullAppList)
        {
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
                            setLim(it)
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