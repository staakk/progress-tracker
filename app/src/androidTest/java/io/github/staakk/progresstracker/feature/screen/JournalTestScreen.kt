package io.github.staakk.progresstracker.feature.screen

import androidx.compose.ui.test.*
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.ui.common.Formatters
import io.github.staakk.progresstracker.ui.journal.JournalTestTags
import io.github.staakk.progresstracker.util.onNodeWithTag
import org.threeten.bp.LocalDate

class JournalTestScreen(
    private val screenContext: ScreenContext,
) {

    private val rule = screenContext.rule

    fun assertNoRoundsMessageIsShown() = apply {
        rule.onNodeWithText(screenContext.getString(R.string.journal_no_round))
            .assertIsDisplayed()
    }

    fun assertRoundIsShown(exerciseName: String, sets: Int, maxWeight: String) = apply {
        rule.onNodeWithText(exerciseName, useUnmergedTree = true)
            .also {
                it.assertIsDisplayed()
                it.onSiblings()
                    .assertAny(hasText(sets.toString()))
                    .assertAny(hasText(maxWeight))
            }
    }

    fun assertDisplaysDate(date: LocalDate) = apply {
        rule.onNodeWithText(date.format(Formatters.DAY_MONTH_SHORT_YEAR_FORMATTER))
            .assertIsDisplayed()
    }

    fun goToNextDay() = apply {
        rule.onNodeWithTag(JournalTestTags.NEXT_DAY)
            .performClick()
    }

    fun goToPrevDay() = apply {
        rule.onNodeWithTag(JournalTestTags.PREV_DAY)
            .performClick()
    }

    fun openCalendar() = apply {
        rule.onNodeWithTag(JournalTestTags.CALENDAR).assertDoesNotExist()
        rule.onNodeWithTag(JournalTestTags.CALENDAR_TOGGLE_BUTTON)
            .performClick()
    }

    fun closeCalendar() = apply {
        rule.onNodeWithTag(JournalTestTags.CALENDAR).assertIsDisplayed()
        rule.onNodeWithTag(JournalTestTags.CALENDAR_TOGGLE_BUTTON)
            .performClick()
    }

    fun selectDate(date: LocalDate) = apply {
        rule.onNodeWithTag(JournalTestTags.CALENDAR).assertIsDisplayed()
        rule.onNodeWithTag(JournalTestTags.CALENDAR)
            .onChildren()
            .filter(hasText(date.dayOfMonth.toString()))
            .let { if (date.dayOfMonth < 15) it.onFirst() else it.onLast() }
            .performClick()
    }

    fun createNewRound(): EditRoundTestScreen {
        rule.onNodeWithText(screenContext.getString(R.string.journal_add_round))
            .performClick()
        return EditRoundTestScreen(screenContext)
    }

    fun editRound(exerciseName: String): EditRoundTestScreen {
        rule.onNodeWithText(exerciseName, useUnmergedTree = true)
            .performClick()
        return EditRoundTestScreen(screenContext)
    }
}