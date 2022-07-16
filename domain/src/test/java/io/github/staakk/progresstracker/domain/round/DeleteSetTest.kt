package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class DeleteSetTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = DeleteSet(mockRoundDataSource)

    private val set = RoundSet(ordinal = 0, reps = 1, weight = 2)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
        roundSets = listOf(set)
    )


    @Test
    fun `should delete set`() {
        coEvery { mockRoundDataSource.deleteSet(set) } returns set.right()

        runBlocking {
            val result = tested(round, set)

            assertEquals(round.copy(roundSets = emptyList()), result.right)
        }
    }

    @Test
    fun `should return error when set cannot be found`() {
        coEvery { mockRoundDataSource.deleteSet(set) } returns RoundDataSource.Error.DeleteSetError.SetNotFound.left()

        runBlocking {
            val result = tested(round, set)

            assertEquals(DeleteSet.Error.SetNotFound, result.left)
        }
    }

}