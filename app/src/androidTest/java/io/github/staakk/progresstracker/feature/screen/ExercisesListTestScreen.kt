package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.*
import io.github.staakk.progresstracker.ui.exercise.search.ExercisesSearchTestTags
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
        rule.onAllNodesWithTag(ExercisesSearchTestTags.ListItem)
            .fetchSemanticsNodes(errorMessageOnFail = "Cannot fetch list items.")
            .size
            .let { assertEquals(names.size, it) }
    }

    fun createNewExercise(): EditExerciseTestScreen {
        rule.onNodeWithTag(ExercisesSearchTestTags.Fab).performClick()
        return EditExerciseTestScreen(context)
    }

    fun selectExercise(name: String): EditExerciseTestScreen {
        rule.onNodeWithText(name).performClick()
        return EditExerciseTestScreen(context)
    }

    fun search(value: String) = apply {
        rule.onNodeWithTag(ExercisesSearchTestTags.Search)
            .performTextInput(value)
    }

    fun clearSearch() = apply {
        rule.onNodeWithTag(ExercisesSearchTestTags.Search)
            .performTextClearance()
    }
}