package io.github.staakk.progresstracker.ui

import NavGraph
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import io.github.staakk.progresstracker.ui.navigation.LocalBackDispatcher

@Composable
fun ProgressTrackerApp(backDispatcher: OnBackPressedDispatcher) {
    Providers(LocalBackDispatcher provides backDispatcher) {
        NavGraph()
    }
}