package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.RoundAlreadyExists
import io.github.staakk.progresstracker.util.functional.Left
import io.github.staakk.progresstracker.util.functional.Right
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class CreateRoundTest {

    private val mockRoundDataSource = mockk<RoundDataSource>()

    private val exercise = Exercise(name = "test")

    private val createdAt = LocalDateTime.now()

    private val round = Round(exercise = exercise, createdAt = createdAt)

    private val tested = CreateRound(mockRoundDataSource)

    private val roundMatcher = { round: Round ->
        round.createdAt == createdAt && round.exercise == exercise
    }

    @Test
    fun `should create round`() {
        coEvery { mockRoundDataSource.create(match(roundMatcher)) } returns round.right()

        runBlocking {
            val result = tested(createdAt, exercise)

            assert(result is Right)
            assertEquals(round, (result as Right).value)
        }
    }

    @Test
    fun `should return error when round already exists`() {
        coEvery { mockRoundDataSource.create(match(roundMatcher)) } returns RoundAlreadyExists.left()

        runBlocking {
            val result = tested(createdAt, exercise)

            assert(result is Left)
            assertEquals(CreateRound.Error.RoundAlreadyExists, (result as Left).value)
        }
    }

}