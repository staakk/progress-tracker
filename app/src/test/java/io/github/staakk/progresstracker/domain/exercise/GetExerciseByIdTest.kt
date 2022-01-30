package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.common.functional.Left
import io.github.staakk.progresstracker.common.functional.Right
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GetExerciseByIdTest {

    private val mockExerciseDataSource = mockk<ExerciseDataSource>()

    private val tested =
        GetExerciseById(mockExerciseDataSource)

    @Test
    fun `should find exercise`() {
        val exercise = Exercise(name = "test")
        coEvery { mockExerciseDataSource.getById(eq(exercise.id)) } returns exercise.right()

        runBlocking {
            val result = tested(exercise.id)
            assert(result is Right)
            assertEquals(exercise, (result as Right).value)
        }
    }

    @Test
    fun `should return error when exercise not found`() {
        val id = "missing id"
        coEvery { mockExerciseDataSource.getById(eq(id)) } returns ExerciseDataSource.Error.ExerciseNotFound.left()

        runBlocking {
            val result = tested(id)

            assert(result is Left)
            assertEquals(GetExerciseById.Error.ExerciseNotFound, (result as Left).value)
        }
    }
}