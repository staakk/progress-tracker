package io.github.staakk.progresstracker.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.github.staakk.progresstracker.ui.navigation.LocalBackDispatcher
import io.github.staakk.progresstracker.ui.navigation.NavGraph

@Composable
fun ProgressTrackerApp(backDispatcher: OnBackPressedDispatcher) {
    CompositionLocalProvider(LocalBackDispatcher provides backDispatcher) {
        NavGraph()
    }
}