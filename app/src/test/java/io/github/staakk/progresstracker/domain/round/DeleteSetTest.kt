package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.LocalDateTime

class DeleteSetTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = DeleteSet(mockRoundDataSource)

    private val set = RoundSet(position = 0, reps = 1, weight = 2)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
        roundSets = listOf(set)
    )


    @Test
    fun `should delete set`() {
        every { mockRoundDataSource.deleteSet(set) } returns set.right()

        val result = tested(round, set)

        assertEquals(round.copy(roundSets = emptyList()), result.right)
    }

    @Test
    fun `should return error when set cannot be found`() {
        every { mockRoundDataSource.deleteSet(set) } returns RoundDataSource.Error.DeleteSetError.SetNotFound.left()

        val result = tested(round, set)

        assertEquals(DeleteSet.Error.SetNotFound, result.left)
    }

}