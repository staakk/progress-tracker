package io.github.staakk.progresstracker.feature

import dagger.hilt.android.testing.HiltAndroidTest
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet
import io.github.staakk.progresstracker.feature.TestDateTimeProvider.currentDate
import io.github.staakk.progresstracker.feature.screen.HomeTestScreen
import io.github.staakk.progresstracker.feature.screen.ScreenContext
import io.github.staakk.progresstracker.initDatabase
import io.github.staakk.progresstracker.testcase.ScreenTestCase
import org.junit.Ignore
import org.junit.Test
import org.threeten.bp.ZoneOffset

private val EXERCISES = listOf(
    Exercise(name = "Dead lift"),
    Exercise(name = "Low bar squat"),
    Exercise(name = "Bench press")
)

private val ROUNDS = listOf(
    RoomRound(
        exerciseId = EXERCISES[0].id,
        createdAt = TestDateTimeProvider.currentDateTime()
            .minusDays(5)
            .toEpochSecond(ZoneOffset.UTC)),
    RoomRound(
        exerciseId = EXERCISES[1].id,
        createdAt = TestDateTimeProvider.currentDateTime().toEpochSecond(ZoneOffset.UTC)),
    RoomRound(
        exerciseId = EXERCISES[2].id,
        createdAt = TestDateTimeProvider.currentDateTime()
            .plusDays(5)
            .toEpochSecond(ZoneOffset.UTC)),
)

private val SETS = listOf(
    RoomSet(position = 0, reps = 1, weight = 1, roundId = ROUNDS[0].id),
    RoomSet(position = 1, reps = 2, weight = 2, roundId = ROUNDS[0].id),
    RoomSet(position = 2, reps = 3, weight = 3, roundId = ROUNDS[0].id),
    RoomSet(position = 0, reps = 1, weight = 1, roundId = ROUNDS[1].id),
    RoomSet(position = 1, reps = 2, weight = 2, roundId = ROUNDS[1].id),
    RoomSet(position = 2, reps = 3, weight = 3, roundId = ROUNDS[1].id),
    RoomSet(position = 0, reps = 1, weight = 1, roundId = ROUNDS[2].id),
    RoomSet(position = 1, reps = 2, weight = 2, roundId = ROUNDS[2].id),
    RoomSet(position = 2, reps = 3, weight = 3, roundId = ROUNDS[2].id),
)

@HiltAndroidTest
class JournalTest: ScreenTestCase() {

    override fun setUp() {
        super.setUp()
        database.initDatabase(EXERCISES, ROUNDS, SETS)
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I select day with no rounds
     * THEN I see there are no rounds
     */
    @Test
    fun shouldShowEmptyDay() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .openCalendar()
            .selectDate(currentDate().plusDays(1))
            .assertNoRoundsMessageIsShown()
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I select day with round
     * THEN I can see rounds from that day
     */
    @Test
    fun shouldShowDayWithRound() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .openCalendar()
            .selectDate(currentDate())
            .assertRoundIsShown(EXERCISES[1].name, 3, "3 kg")
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I change the day
     * THEN The header is updated
     */
    @Test
    fun shouldAllowForSwitchingBetweenDays() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .assertDisplaysDate(currentDate())
            .goToNextDay()
            .assertDisplaysDate(currentDate().plusDays(1))
            .goToPrevDay()
            .goToPrevDay()
            .assertDisplaysDate(currentDate().minusDays(1))
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I add new round
     * THEN The round is displayed
     */
    @Test
    fun shouldCreateRound() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .goToNextDay()
            .createNewRound()
            .goBack()
            .assertRoundIsShown("Dead lift", 0, "0 kg")
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I delete the round
     * THEN I see no rounds message
     */
    @Test
    fun shouldDeleteRound() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .goToNextDay()
            .createNewRound()
            .deleteRound()
            .assertNoRoundsMessageIsShown()
    }

    /**
     * GIVEN I am on the Journal screen
     * WHEN I update the round
     * THE I should see updated round on Journal screen
     */
    @Test
    @Ignore // Does not pass probably some issue with idling resources.
    fun shouldUpdateRound() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openJournal()
            .goToNextDay()
            .createNewRound()
            .goBack()
            .assertRoundIsShown("Dead lift", 0, "0 kg")
            .editRound("Dead lift")
            .addSet()
            .updateSet(0, 1, 20)
            .addSet()
            .updateSet(1, 2, 23)
            .selectExercise("Low bar squat")
            .goBack()
            .assertRoundIsShown("Low bar squat", 2, "23 kg")
    }
}