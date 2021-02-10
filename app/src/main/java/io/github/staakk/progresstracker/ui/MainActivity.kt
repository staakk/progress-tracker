package io.github.staakk.progresstracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.staakk.progresstracker.util.datetime.DateTimeProvider
import io.github.staakk.progresstracker.util.datetime.ProvideDateTimeProvider
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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