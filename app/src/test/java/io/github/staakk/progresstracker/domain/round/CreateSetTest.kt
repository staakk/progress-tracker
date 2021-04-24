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
import java.time.LocalDateTime

class CreateSetTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = CreateSet(mockRoundDataSource)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
    )

    private val set = RoundSet(position = 0, reps = 1, weight = 2)

    private val setMatcher = { it: RoundSet ->
        it.position == set.position &&
                it.reps == set.reps &&
                it.weight == set.weight
    }

    @Test
    fun `should create set`() {
        coEvery {
            mockRoundDataSource.createSet(match(setMatcher), eq(round.id))
        } returns set.right()

        runBlocking {
            val result = tested(round, position = 0, reps = 1, weight = 2)

            assertEquals(round.copy(roundSets = listOf(set)), result.right)
        }
    }

    @Test
    fun `should return error when round does not exist`() {
        coEvery {
            mockRoundDataSource.createSet(match(setMatcher),
                eq(round.id))
        } returns RoundDataSource.Error.CreateSetError.RoundNotFound.left()

        runBlocking {
            val result = tested(round, position = 0, reps = 1, weight = 2)

            assertEquals(CreateSet.Error.RoundNotFound, result.left)
        }
    }

    @Test
    fun `should return error when set already exist`() {
        coEvery {
            mockRoundDataSource.createSet(match(setMatcher),
                eq(round.id))
        } returns RoundDataSource.Error.CreateSetError.SetAlreadyExist.left()

        runBlocking {
            val result = tested(round, position = 0, reps = 1, weight = 2)

            assertEquals(CreateSet.Error.SetAlreadyExists, result.left)
        }
    }
}