package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(round: Round, roundSet: RoundSet): Either<Error, Round> {
        return roundDataSource.deleteSet(roundSet)
            .mapLeft {
                when (it) {
                    RoundDataSource.Error.DeleteSetError.SetNotFound -> Error.SetNotFound
                }
            }
            .map { round.copy(roundSets = round.roundSets - it) }
    }

    sealed class Error {
        object SetNotFound : Error()
    }
}