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

class GetRoundByIdTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    val tested = GetRoundById(mockRoundDataSource)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
    )

    @Test
    fun `should get round by id`() {
        coEvery { mockRoundDataSource.getById(eq(round.id)) } returns round.right()

        runBlocking {
            val result = tested(round.id)

            assertEquals(round, result.right)
        }
    }

    @Test
    fun `should return error when round not found`() {
        coEvery { mockRoundDataSource.getById(eq(round.id)) } returns RoundDataSource.Error.RoundNotFound.left()

        runBlocking {
            val result = tested(round.id)

            assertEquals(GetRoundById.Error.RoundNotFound, result.left)
        }
    }

    @Test
    fun `should have sets sorted ascending by position`() {
        val round = round.copy(
            roundSets = listOf(
                RoundSet(position = 3, reps = 1, weight = 2),
                RoundSet(position = 2, reps = 1, weight = 2),
                RoundSet(position = 5, reps = 1, weight = 2),
            )
        )
        coEvery { mockRoundDataSource.getById(eq(round.id)) } returns round.right()

        runBlocking {
            val result = tested(round.id)

            assertEquals(round.roundSets.sortedBy { it.position }, result.right.roundSets)
        }
    }

}