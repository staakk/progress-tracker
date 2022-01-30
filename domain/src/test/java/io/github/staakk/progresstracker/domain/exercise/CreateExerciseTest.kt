package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.domain.exercise.CreateExercise.Error.ExerciseWithIdAlreadyExists
import io.github.staakk.progresstracker.domain.exercise.CreateExercise.Error.ExerciseWithNameAlreadyExists
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class CreateExerciseTest {

    private val mockExerciseDataSource = mockk<ExerciseDataSource>()

    private val tested =
        CreateExercise(mockExerciseDataSource)

    private val exercise = Exercise(name = "test")

    @Test
    fun `should create exercise`() {
        coEvery { mockExerciseDataSource.findByName(exercise.name) } returns emptyList()
        coEvery { mockExerciseDataSource.create(any()) } returns exercise.right()

        runBlocking {
            val result = tested(exercise.name)
            coVerify { mockExerciseDataSource.create(match { it.name == exercise.name }) }
            assertEquals(result, exercise.right<CreateExercise.Error, Exercise>())
        }
    }

    @Test
    fun `should return error when exercise name already exists`() {
        coEvery { mockExerciseDataSource.findByName(exercise.name) } returns listOf(exercise)

        runBlocking {
            val result = tested(exercise.name)
            coVerify(exactly = 0) { mockExerciseDataSource.create(match { it.name == exercise.name }) }
            assertEquals(
                result,
                ExerciseWithNameAlreadyExists.left<CreateExercise.Error, Exercise>()
            )
        }
    }

    @Test
    fun `should return error when exercise already exists`() {
        coEvery { mockExerciseDataSource.findByName(any()) } returns emptyList()
        coEvery { mockExerciseDataSource.create(any()) } returns ExerciseDataSource.Error.IdAlreadyExists.left()

        runBlocking {
            val result = tested(exercise.name)
            coVerify { mockExerciseDataSource.create(match { it.name == exercise.name }) }
            assertEquals(
                result,
                ExerciseWithIdAlreadyExists.left<CreateExercise.Error, Exercise>()
            )
        }
    }
}