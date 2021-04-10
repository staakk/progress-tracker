package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class UpdateRound @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(
        round: Round,
        exercise: Exercise? = null,
        createdAt: LocalDateTime? = null,
    ): Either<Error, Round> {
        return roundDataSource.update(
            round.copy(
                exercise = exercise ?: round.exercise,
                createdAt = createdAt ?: round.createdAt
            )
        ).mapLeft { Error.RoundNotFound }
    }

    sealed class Error {
        object RoundNotFound : Error()
    }
}