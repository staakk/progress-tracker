package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    operator fun invoke(round: Round, set: Set): Either<Error, Round> {
        return roundDataSource.deleteSet(set, round.id)
            .mapLeft {
                when (it) {
                    RoundDataSource.Error.DeleteSetError.RoundNotFound -> Error.RoundNotFound
                    RoundDataSource.Error.DeleteSetError.SetNotFound -> Error.SetNotFound
                }
            }
            .map { round.copy(sets = round.sets - it) }
    }

    sealed class Error {
        object RoundNotFound : Error()
        object SetNotFound : Error()
    }
}