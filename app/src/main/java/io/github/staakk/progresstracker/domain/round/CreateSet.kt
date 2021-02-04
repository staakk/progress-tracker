package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    operator fun invoke(round: Round, set: Set): Either<Error, Round> {
        return roundDataSource.createSet(set, round.id)
            .mapLeft {
                when (it) {
                    RoundDataSource.Error.CreateSetError.RoundNotFound -> Error.RoundNotFound
                    RoundDataSource.Error.CreateSetError.SetAlreadyExist -> Error.SetAlreadyExists
                }
            }
            .map { round.copy(sets = round.sets + it) }
    }

    sealed class Error {
        object RoundNotFound : Error()
        object SetAlreadyExists : Error()
    }
}