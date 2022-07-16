package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.common.functional.Right
import io.github.staakk.progresstracker.common.functional.right
import io.github.staakk.progresstracker.common.test.collectAll
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.Training
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.migration.RealmMigration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealmTrainingDataSourceTest {

    private lateinit var realm: Realm

    private lateinit var tested: RealmTrainingDataSource

    private lateinit var exercise: Exercise

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
                .deleteRealmIfMigrationNeeded()
                .build()
        )
        tested = RealmTrainingDataSource(realm)
        runBlocking {
            exercise = RealmExerciseDataSource(realm).save(Exercise(name = "Test")).right
        }
    }

    @After
    fun tearDown() {
        realm.writeBlocking {
            deleteAll<RealmExercise>()
            deleteAll<RealmTraining>()
            deleteAll<RealmRound>()
            deleteAll<RealmSet>()
        }
        realm.close()
    }

    @Test
    fun shouldCreateTraining() = runBlocking {
        val training = createTraining()

        val result = tested.saveTraining(training).right

        assertTrue(result.id is Id.RealmId)
        assertEquals(training.date, result.date)
        val round = result.rounds.first()
        val expectedRound = training.rounds.first()
        assertTrue(round.id is Id.RealmId)
        assertEquals(expectedRound.ordinal, round.ordinal)
        val exercise = round.exercise
        val expectedExercise = expectedRound.exercise
        assertTrue(exercise.id is Id.RealmId)
        assertEquals(expectedExercise.name, exercise.name)
        assertEquals(expectedExercise.name, exercise.name)
        val set = round.roundSets.first()
        val expectedSet = expectedRound.roundSets.first()
        assertEquals(expectedSet.ordinal, set.ordinal)
        assertEquals(expectedSet.reps, set.reps)
        assertEquals(expectedSet.weight, set.weight)
    }

    @Test
    fun shouldDeleteTraining() = runBlocking {
        val training = createTraining()
        val saveResult = tested.saveTraining(training)

        val deleteResult = tested.deleteTraining(saveResult.right)

        assertTrue(deleteResult is Right)
        val trainingsCount = realm.query<RealmTraining>().count().find()
        assertEquals(0, trainingsCount)
        val roundsCount = realm.query<RealmRound>().count().find()
        assertEquals(0, roundsCount)
        val setsCount = realm.query<RealmSet>().count().find()
        assertEquals(0, setsCount)
    }

    @Test
    fun shouldEmitTrainingWhenSaved() = runTest {
        val (trainings, job) = collectAll(
            tested.queryTrainingByDate(LocalDateTime.MIN, LocalDateTime.MAX)
        )
        val createdTraining = tested.saveTraining(createTraining()).right

        assertEquals(listOf(listOf(), listOf(createdTraining)), trainings)

        job.cancel()
    }

    @Test
    fun shouldEmitTrainingWhenUpdated() = runTest {
        val (trainings, job) = collectAll(
            tested.queryTrainingByDate(LocalDateTime.MIN, LocalDateTime.MAX)
        )
        val createdTraining = tested.saveTraining(createTraining()).right
        val updatedTraining = tested
            .saveTraining(createdTraining.copy(date = LocalDateTime.ofEpochSecond(1, 0, ZoneOffset.UTC)))
            .right

        assertEquals(
            listOf(
                listOf(),
                listOf(createdTraining),
                listOf(updatedTraining)
            ),
            trainings
        )

        job.cancel()
    }

    private fun createTraining(): Training = Training(
        date = LocalDateTime.of(2022, 11, 11, 11, 11),
        rounds = listOf(
            Round(
                ordinal = 1,
                exercise = exercise,
                roundSets = listOf(
                    RoundSet(
                        ordinal = 1,
                        reps = 1,
                        weight = 2
                    )
                )
            )
        )
    )

}