package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.*
import io.github.staakk.progresstracker.ui.journal.round.EditRoundTags
import io.github.staakk.progresstracker.util.hasTestTag
import io.github.staakk.progresstracker.util.onAllNodesWithTag
import io.github.staakk.progresstracker.util.onNodeWithTag

class EditRoundTestScreen(
    private val screenContext: ScreenContext,
) {

    private val rule = screenContext.rule

    fun assertExerciseSelected(name: String) = apply {
        rule.onNodeWithTag(EditRoundTags.EXERCISE_DROP_DOWN)
            .assert(hasText(name))
    }

    fun selectExercise(name: String) = apply {
        rule.onNodeWithTag(EditRoundTags.EXERCISE_DROP_DOWN)
            .performClick()
        rule.onNode(hasTestTag(EditRoundTags.EXERCISE_DROP_DOWN_ITEM) and hasText(name))
            .performClick()
        assertExerciseSelected(name)
    }

    fun deleteRound(): JournalTestScreen {
        rule.onNodeWithTag(EditRoundTags.DELETE_ROUND)
            .performClick()
        return JournalTestScreen(screenContext)
    }

    fun addSet() = apply {
        rule.onNodeWithTag(EditRoundTags.ADD_SET)
            .performClick()
    }

    fun deleteSet(reps: String, weight: String) = apply {
        rule.onAllNodesWithTag(EditRoundTags.SET)
            .filterToOne(hasAnyChild(hasText(reps)) and hasAnyChild(hasText(weight)))
            .onChildren()
            .filterToOne(hasTestTag(EditRoundTags.DELETE_SET))
            .performClick()
    }

    fun updateSet(index: Int, reps: Int, weight: Int) = apply {
        rule.onAllNodesWithTag(EditRoundTags.SET)[index]
            .onChildren()
            .also {
                it[0].apply {
                    performTextClearance()
                    performTextInput(reps.toString())
                }
                it[1].apply {
                    performTextInput(weight.toString())
                }
            }
    }

    fun goBack(): JournalTestScreen {
        rule.onNodeWithTag(EditRoundTags.BACK)
            .performClick()
        return JournalTestScreen(screenContext)
    }
}