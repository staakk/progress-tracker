package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class DeleteRound @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(round: Round): Either<Error, Round> {
        return roundDataSource.delete(round)
            .mapLeft { Error.RoundNotFound }
    }

    sealed class Error {
        object RoundNotFound : Error()
    }
}