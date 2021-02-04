package io.github.staakk.progresstracker.data.local.round

import io.github.staakk.progresstracker.case.DatabaseTestCase
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.RoomRound.Companion.toRoomRound
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.*
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Left
import io.github.staakk.progresstracker.util.functional.Right
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class LocalRoundDataSourceTest : DatabaseTestCase() {

    private val exercises = listOf(
        Exercise(name = "test 1"),
        Exercise(name = "test 2"),
    )

    private val rounds = listOf(
        Round(
            exercise = exercises[0],
            createdAt = LocalDateTime.of(2020, 10, 1, 12, 0),
        ),
        Round(
            exercise = exercises[0],
            createdAt = LocalDateTime.of(2020, 10, 2, 12, 0),
        ),
        Round(
            exercise = exercises[0],
            createdAt = LocalDateTime.of(2020, 10, 3, 12, 0),
        ),
        Round(
            exercise = exercises[0],
            createdAt = LocalDateTime.of(2020, 10, 4, 12, 0),
        ),
        Round(
            exercise = exercises[0],
            createdAt = LocalDateTime.of(2020, 10, 5, 12, 0),
        ),
    )

    private lateinit var tested: LocalRoundDataSource

    @Before
    override fun setUp() {
        super.setUp()
        exercises.forEach { exerciseDao.create(it) }
        rounds.forEach { roundDao.create(it.toRoomRound()) }
        tested = LocalRoundDataSource(roundDao, setDao)
    }

    @Test
    fun shouldCreateRound() {
        val newRound = Round(exercise = exercises[0], createdAt = LocalDateTime.now().withNano(0))
        val result = tested.create(newRound)

        assert(result is Right)
        assertEquals(roundDao.getById(newRound.id)!!.toRound(), (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenRoundAlreadyExists() {
        val round = rounds.first()
        val result = tested.create(round)

        assert(result is Left)
        assertEquals(RoundAlreadyExists, (result as Left).value)
    }

    @Test
    fun shouldUpdateRound() {
        val newExercise = Exercise(name = "new exercise")
        exerciseDao.create(newExercise)
        val round = rounds.first()
        val result = tested.update(round.copy(exercise = newExercise))

        assert(result is Right)
        assertEquals(roundDao.getById(round.id)!!.toRound(), (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenUpdateNotPossible() {
        val newRound = Round(exercise = exercises[0], createdAt = LocalDateTime.now())
        val result = tested.update(newRound)

        assert(result is Left)
        assertEquals(RoundNotFound, (result as Left).value)
    }

    @Test
    fun shouldDeleteRound() {
        val round = rounds.first()
        val result = tested.delete(round)

        assert(result is Right)
        assertEquals(round, (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenRoundDoesNotExist() {
        val newRound = Round(exercise = exercises[0], createdAt = LocalDateTime.now())
        val result = tested.delete(newRound)

        assert(result is Left)
        assertEquals(RoundNotFound, (result as Left).value)
    }

    @Test
    fun shouldGetRoundById() {
        val result = tested.getById(rounds.first().id)

        assert(result is Right)
        assertEquals(rounds.first(), (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenNotExistingRoundFetched() {
        val newRound = Round(exercise = exercises[0], createdAt = LocalDateTime.now())
        val result = tested.delete(newRound)

        assert(result is Left)
        assertEquals(RoundNotFound, (result as Left).value)
    }

    @Test
    fun shouldFetchRoundsInPeriod() {
        val result = tested.getByDate(rounds[1].createdAt, rounds[3].createdAt)

        assertEquals(3, result.size)
        (1..3).map(rounds::get)
            .forEach { assert(result.contains(it)) }
    }

    @Test
    fun shouldReturnEmptyListForEmptyPeriod() {
        val lastRoundTime = rounds.last().createdAt
        val result = tested.getByDate(lastRoundTime.plusHours(1), lastRoundTime.plusHours(2))

        assert(result.isEmpty())
    }

    @Test
    fun shouldReturnDaysContainingAtLeastOneRound() {
        val expected = rounds.map { LocalDate.from(it.createdAt) }
        val result = tested.getDaysWithRound(rounds.first().createdAt, rounds.last().createdAt)

        assertEquals(expected.sorted(), result.sorted())
    }

    @Test
    fun shouldCreateSet() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()

        val result = tested.createSet(set, round.id)

        assert(result is Right)
        assertEquals(set, (result as Right).value)

        val queriedRound = tested.getById(round.id)
        assert(queriedRound is Right)
        val sets = (queriedRound as Right).value.sets
        assert(sets.isNotEmpty())
        assertEquals(sets, listOf(set))
    }

    @Test
    fun shouldReturnErrorWhenSetIsCreatedForNotExistingRound() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val newRound = Round(exercise = exercises.first(), createdAt = LocalDateTime.now())

        val result = tested.createSet(set, newRound.id)
        assert(result is Left)
        assertEquals(CreateSetError.RoundNotFound, (result as Left).value)
    }

    @Test
    fun shouldReturnErrorWhenSetIsCreatedSecondTime() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()
        tested.createSet(set, round.id)
        val result = tested.createSet(set, round.id)

        assert(result is Left)
        assertEquals(CreateSetError.SetAlreadyExist, (result as Left).value)
    }

    @Test
    fun shouldUpdateSet() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()
        tested.createSet(set, round.id)
        val updatedSet = set.copy(reps = 3)
        val result = tested.updateSet(updatedSet, round.id)

        assert(result is Right)
        assertEquals(updatedSet, (result as Right).value)
    }

    @Test
    fun shouldReturnErrorWhenSetIsUpdatedWithWrongRoundId() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()
        tested.createSet(set, round.id)
        val updatedSet = set.copy(reps = 3)
        val result = tested.updateSet(updatedSet, "wrong id")

        assert(result is Left)
        assertEquals(UpdateSetError.RoundNotFound, (result as Left).value)
    }

    @Test
    fun shouldReturnErrorWhenNotExistingSetIsUpdated() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()
        val result = tested.updateSet(set, round.id)

        assert(result is Left)
        assertEquals(UpdateSetError.SetNotFound, (result as Left).value)
    }

    @Test
    fun shouldDeleteSet() {
        val set = Set(position = 1, reps = 2, weight = 3)
        val round = rounds.first()
        tested.createSet(set, round.id)

        val result = tested.deleteSet(set)
        assert(result is Right)
        assert((tested.getById(round.id) as Right).value.sets.isEmpty())
    }

    @Test
    fun shouldReturnErrorWhenNotExistingSetDeleted() {
        val set = Set(position = 1, reps = 2, weight = 3)

        val result = tested.deleteSet(set)
        assert(result is Left)
        assertEquals(DeleteSetError.SetNotFound, (result as Left).value)
    }

}