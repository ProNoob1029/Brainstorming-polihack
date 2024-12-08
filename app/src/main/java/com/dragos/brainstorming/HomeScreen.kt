package com.dragos.brainstorming

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navToGoodBadApp: () -> Unit,
    navToLimits: () -> Unit,
    navToIsland: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = navToIsland
        ) {
            Text(
                text = "Island",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Button(
            onClick = navToGoodBadApp
        ) {
            Text(
                text = "Manage healthy apps",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Button(
            onClick = navToLimits
        ) {
            Text(
                text = "Manage time limits",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}