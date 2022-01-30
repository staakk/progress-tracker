package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.common.functional.Left
import io.github.staakk.progresstracker.common.functional.Right
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class UpdateExerciseTest {

    private val mockExerciseDataSource = mockk<ExerciseDataSource>()

    private val tested =
        UpdateExercise(mockExerciseDataSource)

    @Test
    fun `should update exercise`() {
        val exercise = Exercise(name = "test")
        val updatedExercise = exercise.copy(name = "changed")
        coEvery { mockExerciseDataSource.findByName(any()) } returns emptyList()
        coEvery { mockExerciseDataSource.update(eq(updatedExercise)) } returns updatedExercise.right()

        runBlocking {
            val result = tested(exercise, updatedExercise.name)

            assert(result is Right)
            assertEquals(updatedExercise, (result as Right).value)
        }
    }

    @Test
    fun `should return error when exercise name already exist`() {
        val exercise = Exercise(name = "test")
        coEvery { mockExerciseDataSource.findByName(any()) } returns listOf(exercise)

        runBlocking {
            val result = tested(exercise, "changed")

            coVerify(exactly = 0) { mockExerciseDataSource.update(any()) }
            assert(result is Left)
            assertEquals(UpdateExercise.Error.NameAlreadyExists, (result as Left).value)
        }
    }

    @Test
    fun `should return error when exercise not found`() {
        val exercise = Exercise(name = "test")
        val updatedExercise = exercise.copy(name = "changed")
        coEvery { mockExerciseDataSource.findByName(any()) } returns emptyList()
        coEvery { mockExerciseDataSource.update(eq(updatedExercise)) } returns ExerciseDataSource.Error.ExerciseNotFound.left()

        runBlocking {
            val result = tested(exercise, updatedExercise.name)

            assert(result is Left)
            assertEquals(UpdateExercise.Error.ExerciseNotFound, (result as Left).value)
        }
    }
}