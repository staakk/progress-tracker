package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.domain.exercise.CreateExercise.Error.ExerciseWithIdAlreadyExists
import io.github.staakk.progresstracker.domain.exercise.CreateExercise.Error.ExerciseWithNameAlreadyExists
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class CreateExerciseTest {

    private val mockExerciseDataSource = mockk<ExerciseDataSource>()

    private val tested = CreateExercise(mockExerciseDataSource)

    @Test
    fun `should create exercise`() {
        val exercise = Exercise(name = "test")
        every { mockExerciseDataSource.findByName(any()) } returns emptyList()
        every { mockExerciseDataSource.create(any()) } returns exercise.right()

        val result = tested(exercise)
        verify { mockExerciseDataSource.create(exercise) }
        assertEquals(result, exercise.right<CreateExercise.Error, Exercise>())
    }

    @Test
    fun `should return error when exercise name already exists`() {
        val exercise = Exercise(name = "test")
        every { mockExerciseDataSource.findByName(any()) } returns listOf(exercise)

        val result = tested(exercise)
        verify(exactly = 0) { mockExerciseDataSource.create(exercise) }
        assertEquals(
            result,
            ExerciseWithNameAlreadyExists.left<CreateExercise.Error, Exercise>()
        )
    }

    @Test
    fun `should return error when exercise already exists`() {
        val exercise = Exercise(name = "test")
        every { mockExerciseDataSource.findByName(any()) } returns emptyList()
        every { mockExerciseDataSource.create(any()) } returns ExerciseDataSource.Error.IdAlreadyExists.left()

        val result = tested(exercise)
        verify { mockExerciseDataSource.create(exercise) }
        assertEquals(
            result,
            ExerciseWithIdAlreadyExists.left<CreateExercise.Error, Exercise>()
        )
    }
}