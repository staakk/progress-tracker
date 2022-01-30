package io.github.staakk.progresstracker.data.local.exercise

import io.github.staakk.progresstracker.testcase.DatabaseTestCase
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.ExerciseNotFound
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.IdAlreadyExists
import io.github.staakk.progresstracker.common.functional.Left
import io.github.staakk.progresstracker.common.functional.Right
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class LocalExerciseDataSourceTest : DatabaseTestCase() {

    private val exercises = listOf(
        Exercise(name = "test 1"),
        Exercise(name = "test 2"),
        Exercise(name = "test 3"),
        Exercise(name = "test 4"),
    )

    private lateinit var tested: LocalExerciseDataSource

    override fun setUp() {
        super.setUp()
        exercises.forEach { exerciseDao.create(it) }
        tested = LocalExerciseDataSource(exerciseDao, Dispatchers.IO)
    }

    @Test
    fun shouldCreateExercise() = runBlocking {
        val newExercise = Exercise(name = "new exercise")
        tested.create(newExercise)
        assertEquals(newExercise, exerciseDao.getById(newExercise.id))
    }

    @Test
    fun shouldReturnErrorWhenExerciseWithExistingIdIsCreated() = runBlocking {
        val exercise = exercises[0]
        val result = tested.create(exercise)
        assert(result is Left)
        assertEquals(IdAlreadyExists, (result as Left).value)
    }

    @Test
    fun shouldUpdateExercise() = runBlocking {
        val exercise = exercises[0]
        val updatedExercise = exercise.copy(name = "changed")
        tested.update(updatedExercise)
        val result = exerciseDao.getById(exercise.id)!!.name

        assertEquals("changed", result)
    }

    @Test
    fun shouldReturnErrorWhenNotExistingExerciseUpdated() = runBlocking {
        val newExercise = Exercise(name = "new exercise")
        val result = tested.update(newExercise)

        assert(result is Left)
        assertEquals(
            ExerciseNotFound,
            (result as Left).value
        )
    }

    @Test
    fun shouldDeleteExercise() = runBlocking {
        val exercise = exercises[0]
        val result = tested.delete(exercise)
        assert(result is Right)
    }

    @Test
    fun shouldReturnErrorWhenNotExistingExerciseDeleted() = runBlocking {
        val newExercise = Exercise(name = "new exercise")
        val result = tested.delete(newExercise)

        assert(result is Left)
        assertEquals(
            ExerciseNotFound,
            (result as Left).value
        )
    }

    @Test
    fun shouldGetExerciseById() = runBlocking {
        val exercise = exercises[0]
        val result = tested.getById(exercise.id)

        assert(result is Right)
        assertEquals(exercise, (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenExerciseDoesNotExist() = runBlocking {
        val newId = UUID.randomUUID().toString()
        val result = tested.getById(newId)

        assert(result is Left)
        assertEquals(ExerciseNotFound, (result as Left).value)
    }

    @Test
    fun shouldFindExerciseByNameSubstring() = runBlocking {
        val exercise = exercises[0]
        val result = tested.findByName("est 1")

        assert(result.size == 1)
        assertEquals(result[0], exercise)
    }

    @Test
    fun shouldFindMultipleExercisesMatching() = runBlocking {
        val result = tested.findByName("est")

        assert(result.size == exercises.size)
        assert(result.containsAll(exercises))
    }

    @Test
    fun shouldFetchAllExercises() = runBlocking {
        val result = tested.getAll()

        assert(result.size == exercises.size)
        assert(result.containsAll(exercises))
    }
}