package io.github.staakk.progresstracker.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ProgressTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

object Dimensions {
    val paddingLarge = 24.dp
    val padding = 16.dp
    val paddingSmall = 8.dp
}