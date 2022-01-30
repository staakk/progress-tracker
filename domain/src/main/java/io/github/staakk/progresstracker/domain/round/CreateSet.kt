package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.common.functional.Either
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(
        round: Round,
        position: Int,
        reps: Int,
        weight: Int,
    ): Either<Error, Round> {
        return roundDataSource.createSet(
            RoundSet(position = position, reps = reps, weight = weight),
            round.id,
        )
            .mapLeft {
                when (it) {
                    RoundDataSource.Error.CreateSetError.RoundNotFound -> Error.RoundNotFound
                    RoundDataSource.Error.CreateSetError.SetAlreadyExist -> Error.SetAlreadyExists
                }
            }
            .map { round.copy(roundSets = round.roundSets + it) }
    }

    sealed class Error {
        object RoundNotFound : Error()
        object SetAlreadyExists : Error()
    }
}