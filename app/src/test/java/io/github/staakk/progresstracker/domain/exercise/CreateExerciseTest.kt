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

    private val exercise = Exercise(name = "test")

    @Test
    fun `should create exercise`() {
        every { mockExerciseDataSource.findByName(exercise.name) } returns emptyList()
        every { mockExerciseDataSource.create(any()) } returns exercise.right()

        val result = tested(exercise.name)
        verify { mockExerciseDataSource.create(match { it.name == exercise.name }) }
        assertEquals(result, exercise.right<CreateExercise.Error, Exercise>())
    }

    @Test
    fun `should return error when exercise name already exists`() {
        every { mockExerciseDataSource.findByName(exercise.name) } returns listOf(exercise)

        val result = tested(exercise.name)
        verify(exactly = 0) { mockExerciseDataSource.create(match { it.name == exercise.name }) }
        assertEquals(
            result,
            ExerciseWithNameAlreadyExists.left<CreateExercise.Error, Exercise>()
        )
    }

    @Test
    fun `should return error when exercise already exists`() {
        every { mockExerciseDataSource.findByName(any()) } returns emptyList()
        every { mockExerciseDataSource.create(any()) } returns ExerciseDataSource.Error.IdAlreadyExists.left()

        val result = tested(exercise.name)
        verify { mockExerciseDataSource.create(match { it.name == exercise.name }) }
        assertEquals(
            result,
            ExerciseWithIdAlreadyExists.left<CreateExercise.Error, Exercise>()
        )
    }
}