package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.*
import io.github.staakk.progresstracker.ui.exercise.ExercisesListTestTags
import io.github.staakk.progresstracker.util.onAllNodesWithTag
import io.github.staakk.progresstracker.util.onNodeWithTag
import org.junit.Assert.assertEquals

class ExercisesListTestScreen(
    private val context: ScreenContext,
) {

    private val rule = context.rule

    fun assertShowsExercises(vararg names: String) = apply {
        names.forEach {
            rule.onNodeWithText(it).assertIsDisplayed()
        }
        rule.onAllNodesWithTag(ExercisesListTestTags.LIST_ITEM)
            .fetchSemanticsNodes(errorMessageOnFail = "Cannot fetch list items.")
            .size
            .let { assertEquals(names.size, it) }
    }

    fun createNewExercise(): EditExerciseTestScreen {
        rule.onNodeWithTag(ExercisesListTestTags.FAB).performClick()
        return EditExerciseTestScreen(context)
    }

    fun selectExercise(name: String): EditExerciseTestScreen {
        rule.onNodeWithText(name).performClick()
        return EditExerciseTestScreen(context)
    }

    fun search(value: String) = apply {
        rule.onNodeWithTag(ExercisesListTestTags.SEARCH)
            .performTextInput(value)
    }

    fun clearSearch() = apply {
        rule.onNodeWithTag(ExercisesListTestTags.SEARCH)
            .performTextClearance()
    }
}