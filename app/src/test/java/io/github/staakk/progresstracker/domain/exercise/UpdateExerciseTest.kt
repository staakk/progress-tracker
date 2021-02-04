package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.util.functional.Left
import io.github.staakk.progresstracker.util.functional.Right
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class UpdateExerciseTest {

    private val mockExerciseDataSource = mockk<ExerciseDataSource>()

    private val tested = UpdateExercise(mockExerciseDataSource)

    @Test
    fun `should update exercise`() {
        val exercise = Exercise(name = "test")
        val updatedExercise = exercise.copy(name = "changed")
        every { mockExerciseDataSource.findByName(any()) } returns emptyList()
        every { mockExerciseDataSource.update(eq(updatedExercise)) } returns updatedExercise.right()

        val result = tested(exercise, updatedExercise.name)

        assert(result is Right)
        assertEquals(updatedExercise, (result as Right).value)
    }

    @Test
    fun `should return error when exercise name already exist`() {
        val exercise = Exercise(name = "test")
        every { mockExerciseDataSource.findByName(any()) } returns listOf(exercise)

        val result = tested(exercise, "changed")

        verify(exactly = 0) { mockExerciseDataSource.update(any()) }
        assert(result is Left)
        assertEquals(UpdateExercise.Error.NameAlreadyExists, (result as Left).value)
    }

    @Test
    fun `should return error when exercise not found`() {
        val exercise = Exercise(name = "test")
        val updatedExercise = exercise.copy(name = "changed")
        every { mockExerciseDataSource.findByName(any()) } returns emptyList()
        every { mockExerciseDataSource.update(eq(updatedExercise)) } returns ExerciseDataSource.Error.ExerciseNotFound.left()

        val result = tested(exercise, updatedExercise.name)

        assert(result is Left)
        assertEquals(UpdateExercise.Error.ExerciseNotFound, (result as Left).value)
    }
}