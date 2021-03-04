package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.*
import io.github.staakk.progresstracker.ui.exercise.ExercisesListTestTags
import junit.framework.Assert.assertEquals

class ExercisesListTestScreen(
    private val context: ScreenContext,
) {

    private val rule = context.rule

    fun assertShowsExercises(vararg names: String) = apply {
        names.forEach {
            rule.onNodeWithText(it).assertIsDisplayed()
        }
        rule.onAllNodesWithTag(ExercisesListTestTags.LIST_ITEM.name)
            .fetchSemanticsNodes(errorMessageOnFail = "Cannot fetch list items.")
            .size
            .let { assertEquals(names.size, it) }
    }

    fun createNewExercise(): EditExerciseTestScreen {
        rule.onNodeWithTag(ExercisesListTestTags.FAB.name).performClick()
        return EditExerciseTestScreen(context)
    }

    fun selectExercise(name: String): EditExerciseTestScreen {
        rule.onNodeWithText(name).performClick()
        return EditExerciseTestScreen(context)
    }

    fun search(value: String) = apply {
        rule.onNodeWithTag(ExercisesListTestTags.SEARCH.name)
            .performTextInput(value)
    }

    fun clearSearch() = apply {
        rule.onNodeWithTag(ExercisesListTestTags.SEARCH.name)
            .performTextClearance()
    }
}