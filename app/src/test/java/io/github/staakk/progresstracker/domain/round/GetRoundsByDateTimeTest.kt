package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundSet
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class GetRoundsByDateTimeTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = GetRoundsByDateTime(mockRoundDataSource)

    @Test
    fun `should return rounds in time span`() {
        every { mockRoundDataSource.getByDate(any(), any()) } returns emptyList()

        val result = tested(LocalDateTime.now(), LocalDateTime.now().plusHours(1))

        assertEquals(emptyList<Round>(), result)
    }

    @Test
    fun `should return rounds with sets sorted by position`() {
        val rounds = listOf(
            Round(
                exercise = Exercise(name = "test"),
                createdAt = LocalDateTime.now().plusMinutes(30),
                roundSets = listOf(
                    RoundSet(position = 1, reps = 1, weight = 2),
                    RoundSet(position = 0, reps = 1, weight = 2),
                )
            )
        )
        every { mockRoundDataSource.getByDate(any(), any()) } returns rounds

        val result = tested(LocalDateTime.now(), LocalDateTime.now().plusHours(1))

        assertEquals(
            rounds.first().roundSets.sortedBy { it.position },
            result.first().roundSets
        )
    }

    @Test
    fun `should return empty list when from date after to date`() {
        val result = tested(LocalDateTime.now().plusDays(1), LocalDateTime.now())

        assertEquals(emptyList<Round>(), result)
        verify(exactly = 0) { mockRoundDataSource.getByDate(any(), any()) }
    }

    @Test
    fun `should get round for given day`() {
        every { mockRoundDataSource.getByDate(any(), any()) } returns emptyList()

        val result = tested(LocalDate.now())

        assertEquals(emptyList<Round>(), result)
        verify {
            mockRoundDataSource.getByDate(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atStartOfDay().plusHours(24))
        }
    }

}