package io.github.staakk.progresstracker.testcase

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import io.github.staakk.progresstracker.ComposeIdlingResource
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.ui.MainActivity
import io.github.staakk.progresstracker.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class ScreenTestCase {

    @Suppress("LeakingThis")
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var database: AppDatabase

    private val composeIdlingResource = ComposeIdlingResource(EspressoIdlingResource.countingIdlingResource)

    @Before
    open fun setUp() {
        composeTestRule.registerIdlingResource(composeIdlingResource)
        hiltRule.inject()
    }

    @After
    open fun tearDown() {
        composeTestRule.unregisterIdlingResource(composeIdlingResource)
    }
}