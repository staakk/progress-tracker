package io.github.staakk.progresstracker.feature.screen

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.github.staakk.progresstracker.ui.MainActivity

class ScreenContext(
    val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    val activity: MainActivity = rule.activity
) {
    fun getString(@StringRes id: Int) = activity.getString(id)
}