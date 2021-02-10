package io.github.staakk.progresstracker.feature

import dagger.hilt.android.testing.HiltAndroidTest
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.feature.screen.HomeTestScreen
import io.github.staakk.progresstracker.feature.screen.ScreenContext
import io.github.staakk.progresstracker.initDatabase
import io.github.staakk.progresstracker.testcase.ScreenTestCase
import org.junit.Before
import org.junit.Test

private val EXERCISES = listOf(
    Exercise(name = "Dead lift"),
    Exercise(name = "Low bar squat"),
    Exercise(name = "Bench press")
)

@HiltAndroidTest
class ExerciseListTest : ScreenTestCase() {

    @Before
    override fun setUp() {
        super.setUp()
        database.initDatabase(EXERCISES)
    }

    /**
     * GIVEN I am on exercises list screen
     * WHEN I type in search string
     * THEN I see only matching exercises
     */
    @Test
    fun shouldPerformSearchOnExercisesList() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openExercisesList()
            .assertShowsExercises("Dead lift", "Bench press", "Low bar squat")
            .search("Bench")
            .assertShowsExercises("Bench press")
            .clearSearch()
            .assertShowsExercises("Dead lift", "Bench press", "Low bar squat")
    }

    /**
     * GIVEN I am on exercises list screen
     * WHEN I select exercise
     * THEN I want to be able to change it's name
     */
    @Test
    fun shouldChangeExerciseName() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openExercisesList()
            .assertShowsExercises("Dead lift", "Bench press", "Low bar squat")
            .selectExercise("Low bar squat")
            .setExerciseName("High bar squat")
            .saveChanges()
            .assertShowsExercises("Dead lift", "Bench press", "High bar squat")
    }

    /**
     * GIVEN I am on exercises list screen
     * WHEN I create new exercise
     * THEN It is visible on the list
     */
    @Test
    fun shouldCreateNewExercise() {
        HomeTestScreen(ScreenContext(composeTestRule))
            .openExercisesList()
            .assertShowsExercises("Dead lift", "Bench press", "Low bar squat")
            .createNewExercise()
            .setExerciseName("High bar squat")
            .saveChanges()
            .assertShowsExercises("Dead lift", "Bench press", "Low bar squat", "High bar squat")
    }
}