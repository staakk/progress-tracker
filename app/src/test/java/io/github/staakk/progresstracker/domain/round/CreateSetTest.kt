package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.LocalDateTime

class CreateSetTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = CreateSet(mockRoundDataSource)

    private val round = Round(
        exercise = Exercise(name = "test"),
        createdAt = LocalDateTime.now(),
    )

    private val set = RoundSet(position = 0, reps = 1, weight = 2)

    @Test
    fun `should create set`() {
        every { mockRoundDataSource.createSet(eq(set), eq(round.id)) } returns set.right()

        val result = tested(round, set)

        assertEquals(round.copy(roundSets = listOf(set)), result.right)
    }

    @Test
    fun `should return error when round does not exist`() {
        every { mockRoundDataSource.createSet(eq(set), eq(round.id)) } returns RoundDataSource.Error.CreateSetError.RoundNotFound.left()

        val result = tested(round, set)

        assertEquals(CreateSet.Error.RoundNotFound, result.left)
    }

    @Test
    fun `should return error when set already exist`() {
        every { mockRoundDataSource.createSet(eq(set), eq(round.id)) } returns RoundDataSource.Error.CreateSetError.SetAlreadyExist.left()

        val result = tested(round, set)

        assertEquals(CreateSet.Error.SetAlreadyExists, result.left)
    }
}