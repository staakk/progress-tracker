package io.github.staakk.progresstracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.staakk.progresstracker.ui.journal.JournalViewModel
import io.github.staakk.progresstracker.util.datetime.DateTimeProvider
import io.github.staakk.progresstracker.util.datetime.ProvideDateTimeProvider
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dateTimeProvider: DateTimeProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Providers(LocalViewModelFactoryProvider provides defaultViewModelProviderFactory) {
                ProvideDateTimeProvider(dateTimeProvider = dateTimeProvider) {
                    ProgressTrackerApp(backDispatcher = onBackPressedDispatcher)
                }
            }
        }
    }
}

internal val LocalViewModelFactoryProvider = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("No Back Dispatcher provided")
}