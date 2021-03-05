package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.ui.exercise.editexercise.EditExerciseScreenTags.*
import io.github.staakk.progresstracker.util.onNodeWithTag

class EditExerciseTestScreen(
    private val context: ScreenContext,
) {
    private val rule = context.rule

    fun setExerciseName(name: String) = apply {
        rule.onNodeWithTag(NAME).apply {
            performTextClearance()
            performTextInput(name)
        }
    }

    fun saveChanges(): ExercisesListTestScreen {
        rule.onNodeWithText(context.getString(R.string.done)).performClick()
        return ExercisesListTestScreen(context)
    }

    fun discardChanges(): ExercisesListTestScreen {
        rule.onNodeWithText(context.getString(R.string.cancel)).performClick()
        return ExercisesListTestScreen(context)
    }
}