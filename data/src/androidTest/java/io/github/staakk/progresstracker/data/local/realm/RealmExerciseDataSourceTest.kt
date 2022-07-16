package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.common.functional.Left
import io.github.staakk.progresstracker.common.functional.Right
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.ExerciseNotFound
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RealmExerciseDataSourceTest {
    private lateinit var realm: Realm
    private lateinit var tested: RealmExerciseDataSource

    @Before
    fun setUp() {
        realm = Realm.open(
            RealmConfiguration
                .Builder(
                    setOf(
                        RealmExercise::class,
                        RealmTraining::class,
                        RealmRound::class,
                        RealmSet::class
                    )
                )
                .build()
        )
        tested = RealmExerciseDataSource(realm)
    }

    @After
    fun tearDown() {
        realm.writeBlocking {
            val exercises = query<RealmExercise>().find()
            delete(exercises)
        }
        realm.close()
    }

    @Test
    fun shouldCreateExercise() = runBlocking {
        val newExercise = tested.save(Exercise(name = "new exercise")).right
        assertEquals(newExercise, tested.getById(newExercise.id).right)
    }

    @Test
    fun shouldUpdateExercise() = runBlocking {
        val newExercise = tested.save(Exercise(name = "new exercise")).right
        tested.save(newExercise.copy(name = "changed"))
        assertEquals(
            "changed",
            tested.getById(newExercise.id).right.name
        )
    }

    @Test
    fun shouldDeleteExercise() = runBlocking {
        val exercise = tested.save(Exercise(name = "test_name")).right


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
        val exercise = tested.save(Exercise(name = "test_name")).right

        val result = tested.getById(exercise.id)

        assert(result is Right)
        assertEquals(exercise, (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenExerciseDoesNotExist() = runBlocking {
        val exercise = Exercise(name = "test_exercise")
        val result = tested.getById(exercise.id)

        assert(result is Left)
        assertEquals(ExerciseNotFound, (result as Left).value)
    }

    @Test
    fun shouldFindExerciseByNameSubstring() = runBlocking {
        val exercise = tested.save(Exercise(name = "Test 1")).right
        val result = tested
            .findByNameContains("est")
            .first()

        assertEquals(result, listOf(exercise))
    }

    @Test
    fun shouldFindMultipleExercisesMatching() = runBlocking {
        val exercises = listOf(
            Exercise(name = "Test 1"),
            Exercise(name = "Test 2"),
        )
            .map { tested.save(it).right }
        val result = tested
            .findByNameContains("est")
            .first()

        assert(result.size == exercises.size)
        assert(result.containsAll(exercises))
    }

    @Test
    fun shouldFetchAllExercises() = runBlocking {
        val exercises = listOf(
            Exercise(name = "Test 1"),
            Exercise(name = "Test 2"),
        )
            .map { tested.save(it).right }

        val result = tested.getAll()

        assert(result.size == exercises.size)
        assert(result.containsAll(exercises))
    }
}