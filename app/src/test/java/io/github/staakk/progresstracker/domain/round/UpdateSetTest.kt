package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.LocalDateTime

class UpdateSetTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = UpdateSet(mockRoundDataSource)

    private val set = RoundSet(position = 0, reps = 1, weight = 2)

    private val updatedSet = set.copy(position = 1, reps = 2, weight = 3)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
        roundSets = listOf(set)
    )

    private val updatedRound = round.copy(roundSets = listOf(updatedSet))

    @Test
    fun `should update set`() {
        coEvery { mockRoundDataSource.updateSet(updatedSet, round.id) } returns updatedSet.right()

        runBlocking {
            val result = tested(round, set, updatedSet.position, updatedSet.reps, updatedSet.weight)
            val (resultRound, resultSet) = result.right
            assertEquals(updatedRound, resultRound)
            assertEquals(updatedSet, resultSet)
        }
    }

    @Test
    fun `should return error when round not found`() {
        coEvery {
            mockRoundDataSource.updateSet(updatedSet,
                round.id)
        } returns RoundDataSource.Error.UpdateSetError.RoundNotFound.left()

        runBlocking {
            val result = tested(round, set, updatedSet.position, updatedSet.reps, updatedSet.weight)

            assertEquals(UpdateSet.Error.RoundNotFound, result.left)
        }
    }

    @Test
    fun `should return error when set not found`() {
        coEvery {
            mockRoundDataSource.updateSet(updatedSet,
                round.id)
        } returns RoundDataSource.Error.UpdateSetError.SetNotFound.left()

        runBlocking {
            val result = tested(round, set, updatedSet.position, updatedSet.reps, updatedSet.weight)

            assertEquals(UpdateSet.Error.SetNotFound, result.left)
        }
    }

}