package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class DeleteRoundTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = DeleteRound(mockRoundDataSource)

    private val round = Round(
        exercise = Exercise(name = "name"),
        createdAt = LocalDateTime.now(),
    )

    @Test
    fun `should delete round`() {
        coEvery { mockRoundDataSource.delete(eq(round)) } returns round.right()

        runBlocking {
            val result = tested(round)

            assertEquals(round, result.right)
        }
    }

    @Test
    fun `should return error when round does not exist`() {
        coEvery { mockRoundDataSource.delete(eq(round)) } returns RoundDataSource.Error.RoundNotFound.left()

        runBlocking {
            val result = tested(round)

            assertEquals(DeleteRound.Error.RoundNotFound, result.left)
        }
    }
}