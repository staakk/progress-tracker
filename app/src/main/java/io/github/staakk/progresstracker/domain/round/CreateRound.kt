package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CreateRound @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    operator fun invoke(createdAt: LocalDate, exercise: Exercise): Either<Error, Round> {
        return roundDataSource.create(Round(exercise = exercise,
            createdAt = createdAt.atStartOfDay()))
            .mapLeft { Error.RoundNotFound }
    }

    sealed class Error {
        object RoundNotFound : Error()
    }
}