package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class CreateRound @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    operator fun invoke(createdAt: LocalDateTime, exercise: Exercise): Either<Error, Round> {
        return roundDataSource.create(Round(exercise = exercise,
            createdAt = createdAt))
            .mapLeft { Error.RoundAlreadyExists }
    }

    sealed class Error {
        object RoundAlreadyExists : Error()
    }
}