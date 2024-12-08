package com.dragos.brainstorming

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navToGoodBadApp: () -> Unit,
    navToLimits: () -> Unit,
    navToIsland: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            OutlinedButton(
                onClick = navToIsland,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Island",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = navToGoodBadApp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Manage healthy apps",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = navToLimits,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Manage time limits",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}