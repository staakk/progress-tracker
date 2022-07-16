package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class UpdateRoundTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = UpdateRound(mockRoundDataSource)

    private val exercise1 = Exercise(name = "test 1")

    private val exercise2 = Exercise(name = "test 2")

    private val updatedTime = LocalDateTime.now()

    private val round = Round(
        exercise = exercise1,
        createdAt = LocalDateTime.now(),
    )

    private val updatedRound = round.copy(
        exercise = exercise2,
        createdAt = updatedTime,
    )

    @Test
    fun `should update round`() {
        coEvery { mockRoundDataSource.update(eq(updatedRound)) } returns updatedRound.right()

        runBlocking {
            val result = tested(round, exercise2, updatedTime)

            assertEquals(updatedRound, result.right)
        }
    }

    @Test
    fun `should return error when round does not exist`() {
        coEvery { mockRoundDataSource.update(eq(updatedRound)) } returns RoundDataSource.Error.RoundNotFound.left()

        runBlocking {
            val result = tested(round, exercise2, updatedTime)

            assertEquals(UpdateRound.Error.RoundNotFound, result.left)
        }
    }
}