package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.staakk.progresstracker.R

class HomeTestScreen(
    private val context: ScreenContext
) {
    private val rule = context.rule

    fun openExercisesList(): ExercisesListTestScreen {
        rule.onNodeWithText(context.getString(R.string.home_browse_exercises))
            .performClick()
        return ExercisesListTestScreen(context)
    }
}