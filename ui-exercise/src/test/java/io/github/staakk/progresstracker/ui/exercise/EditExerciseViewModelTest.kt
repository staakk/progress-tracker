package io.github.staakk.progresstracker.ui.exercise

import io.github.staakk.progresstracker.common.functional.right
import io.github.staakk.progresstracker.common.test.MainDispatcherRule
import io.github.staakk.progresstracker.common.test.collectAll
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExerciseById
import io.github.staakk.progresstracker.domain.exercise.SaveExercise
import io.github.staakk.progresstracker.ui.exercise.EditExerciseEvent.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class EditExerciseViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getExerciseById = mockk<GetExerciseById>()
    private val saveExercise = mockk<SaveExercise>()
    private val tested = EditExerciseViewModel(
        getExerciseById,
        saveExercise,
    )

    private val testExercise = Exercise(id = "test_id", name = "test_name")

    @Test
    fun `should load exercise when opened with id`() = runTest {
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        mockGetExerciseById(testExercise)

        tested.dispatch(ScreenOpened(testExercise.id))

        assertEquals(
            listOf(
                EditExerciseState.Loading,
                EditExerciseState.Editing(false, testExercise),
            ),
            states
        )
        job.cancel()
    }

    @Test
    fun `should create exercise when opened without id`() = runTest {
        val uuid = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        tested.dispatch(ScreenOpened(null))

        assertEquals(
            listOf(
                EditExerciseState.Loading,
                EditExerciseState.Editing(true, Exercise(uuid.toString(), "")),
            ),
            states
        )

        job.cancel()
        unmockkStatic(UUID::class)
    }

    @Test
    fun `should update exercise name when name changed`() = runTest {
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        mockGetExerciseById(testExercise)
        tested.dispatch(ScreenOpened(testExercise.id))

        tested.dispatch(ExerciseNameChanged("new_name"))

        assertEquals(
            listOf(
                EditExerciseState.Loading,
                EditExerciseState.Editing(false, testExercise),
                EditExerciseState.Editing(false, testExercise.copy(name = "new_name")),
            ),
            states
        )

        job.cancel()
    }

    @Test
    fun `should not update exercise name when exercise is not loaded`() = runTest {
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        mockGetExerciseById(testExercise)

        tested.dispatch(ExerciseNameChanged("new_name"))

        assertEquals(
            listOf(EditExerciseState.Loading),
            states
        )

        job.cancel()
    }

    @Test
    fun `should not update exercise name when name did not changed`() = runTest {
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        mockGetExerciseById(testExercise)
        tested.dispatch(ScreenOpened("test_id"))

        tested.dispatch(ExerciseNameChanged("test_name"))

        assertEquals(
            listOf(
                EditExerciseState.Loading,
                EditExerciseState.Editing(false, Exercise("test_id", "test_name")),
            ),
            states
        )

        job.cancel()
    }

    @Test
    fun `should save exercise`() = runTest {
        val (states, job) = collectAll(tested.state, dispatcherRule.dispatcher)
        mockGetExerciseById(testExercise)
        coEvery { saveExercise(testExercise) } returns testExercise.right()
        tested.dispatch(ScreenOpened("test_id"))

        tested.dispatch(SaveExerciseClicked)

        assertEquals(
            listOf(
                EditExerciseState.Loading,
                EditExerciseState.Editing(false, testExercise),
                EditExerciseState.Saving,
                EditExerciseState.Saved,
            ),
            states
        )

        job.cancel()
    }

    private fun mockGetExerciseById(exercise: Exercise) {
        coEvery { getExerciseById(exercise.id) } returns exercise.right()
    }
}
