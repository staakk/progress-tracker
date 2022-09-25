package io.github.staakk.progresstracker.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.staakk.common.ui.compose.LocalActivity
import io.github.staakk.common.ui.compose.datetime.DateTimeProvider
import io.github.staakk.common.ui.compose.datetime.ProvideDateTimeProvider
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var dateTimeProvider: DateTimeProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgressTrackerTheme {
                CompositionLocalProvider(LocalActivity provides this) {
                    ProvideDateTimeProvider(dateTimeProvider = dateTimeProvider) {
                        ProgressTrackerApp(backDispatcher = onBackPressedDispatcher)
                    }
                }
            }
        }
    }
}
