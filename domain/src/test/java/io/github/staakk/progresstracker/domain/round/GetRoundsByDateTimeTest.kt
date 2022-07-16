package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetRoundsByDateTimeTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val tested = GetRoundsByDateTime(mockRoundDataSource)

    @Test
    fun `should return rounds in time span`() {
        coEvery { mockRoundDataSource.getByDate(any(), any()) } returns emptyList()

        runBlocking {
            val result = tested(LocalDateTime.now(), LocalDateTime.now().plusHours(1))

            assertEquals(emptyList<Round>(), result)
        }
    }

    @Test
    fun `should return rounds with sets sorted by position`() {
        val rounds = listOf(
            Round(
                exercise = Exercise(name = "test"),
                createdAt = LocalDateTime.now().plusMinutes(30),
                roundSets = listOf(
                    RoundSet(ordinal = 1, reps = 1, weight = 2),
                    RoundSet(ordinal = 0, reps = 1, weight = 2),
                )
            )
        )
        coEvery { mockRoundDataSource.getByDate(any(), any()) } returns rounds

        runBlocking {
            val result = tested(LocalDateTime.now(), LocalDateTime.now().plusHours(1))

            assertEquals(
                rounds.first().roundSets.sortedBy { it.ordinal },
                result.first().roundSets
            )
        }
    }

    @Test
    fun `should return empty list when from date after to date`() {
        runBlocking {
            val result = tested(LocalDateTime.now().plusDays(1), LocalDateTime.now())

            assertEquals(emptyList<Round>(), result)
            coVerify(exactly = 0) { mockRoundDataSource.getByDate(any(), any()) }
        }
    }

    @Test
    fun `should get round for given day`() {
        coEvery { mockRoundDataSource.getByDate(any(), any()) } returns emptyList()

        runBlocking {
            val result = tested(LocalDate.now())

            assertEquals(emptyList<Round>(), result)
            coVerify {
                mockRoundDataSource.getByDate(
                    LocalDate.now().atStartOfDay(),
                    LocalDate.now().atStartOfDay().plusHours(24))
            }
        }
    }

}