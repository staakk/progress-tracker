package io.github.staakk.progresstracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.staakk.common.ui.compose.datetime.DateTimeProvider
import io.github.staakk.common.ui.compose.datetime.ProvideDateTimeProvider
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dateTimeProvider: DateTimeProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideDateTimeProvider(dateTimeProvider = dateTimeProvider) {
                ProgressTrackerApp(backDispatcher = onBackPressedDispatcher)
            }
        }
    }
}
