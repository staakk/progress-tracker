package io.github.staakk.progresstracker.ui.exercise

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.ui.exercise.EditExerciseScreenTags.NameField
import io.github.staakk.progresstracker.ui.exercise.EditExerciseScreenTags.ProgressIndicator
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class EditExerciseScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUpLogging() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `should show loading state`() {
        composeRule.apply {
            showState(EditExerciseState.Loading)

            onNodeWithTag(ProgressIndicator).assertIsDisplayed()
        }
    }

    @Test
    fun `should show editing state for new exercise`() {
        composeRule.apply {
            showState(EditExerciseState.Editing(true, Exercise("id", "")))

            onNodeWithTag(ProgressIndicator).assertDoesNotExist()
            onNodeWithTag(NameField, useUnmergedTree = true)
                .assertTextEquals("")
            onNodeWithText("Create new exercise", useUnmergedTree = true)
                .assertIsDisplayed()
        }
    }

    @Test
    fun `should show editing state for existing exercise`() {
        composeRule.apply {
            showState(EditExerciseState.Editing(false, Exercise("id", "name")))

            onNodeWithTag(ProgressIndicator).assertDoesNotExist()
            onNodeWithTag(NameField, useUnmergedTree = true)
                .assertTextEquals("name")
            onNodeWithText("Edit exercise", useUnmergedTree = true)
                .assertIsDisplayed()
        }
    }

    @Test
    fun `should show saving state`() {
        composeRule.apply {
            showState(EditExerciseState.Saving)

            onNodeWithTag(ProgressIndicator).assertIsDisplayed()
        }
    }

    @Test
    fun `saved state should navigate up`() {
        val navigateUp = mockk<() -> Unit> {
            justRun { this@mockk() }
        }
        composeRule.apply {
            composeRule.setContent {
                EditExerciseScreen(
                    state = EditExerciseState.Saved,
                    dispatch = {},
                    navigateUp = navigateUp,
                )
            }

            verify { navigateUp() }
        }
    }

    @Test
    fun `error state should navigate up`() {
        val navigateUp = mockk<() -> Unit> {
            justRun { this@mockk() }
        }
        composeRule.apply {
            composeRule.setContent {
                EditExerciseScreen(
                    state = EditExerciseState.Error,
                    dispatch = {},
                    navigateUp = navigateUp,
                )
            }

            verify { navigateUp() }
        }
    }

    private fun showState(state: EditExerciseState) {
        composeRule.setContent {
            EditExerciseScreen(
                state = state,
                dispatch = {},
                navigateUp = {},
            )
        }
    }
}
