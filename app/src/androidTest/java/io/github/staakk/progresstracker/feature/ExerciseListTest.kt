package io.github.staakk.progresstracker.feature

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.staakk.progresstracker.ComposeIdlingResource
import io.github.staakk.progresstracker.DbHelper
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.feature.screen.HomeTestScreen
import io.github.staakk.progresstracker.feature.screen.ScreenContext
import io.github.staakk.progresstracker.ui.MainActivity
import io.github.staakk.progresstracker.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ExerciseListTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var database: AppDatabase

    private val composeIdlingResource = ComposeIdlingResource(EspressoIdlingResource.countingIdlingResource)

    @Before
    fun setUp() {
        composeTestRule.registerIdlingResource(composeIdlingResource)
        hiltRule.inject()
        DbHelper.initDatabase(database)
    }

    @After
    fun tearDown() {
        composeTestRule.unregisterIdlingResource(composeIdlingResource)
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