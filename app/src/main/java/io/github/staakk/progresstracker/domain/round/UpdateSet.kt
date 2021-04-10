package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.UpdateSetError.RoundNotFound
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.UpdateSetError.SetNotFound
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class UpdateSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(
        round: Round,
        roundSet: RoundSet,
        position: Int? = null,
        reps: Int? = null,
        weight: Int? = null,
    ): Either<Error, Result> {
        return roundDataSource.updateSet(
            roundSet.copy(
                position = position ?: roundSet.position,
                reps = reps ?: roundSet.reps,
                weight = weight ?: roundSet.weight,
            ),
            round.id
        )
            .map { updatedSet ->
                val updatedSets = round.roundSets.filter { it.id != roundSet.id } + updatedSet
                val updatedRound = round.copy(roundSets = updatedSets).withPositionSortedSets()
                Result(updatedRound, updatedSet)
            }
            .mapLeft {
                when (it) {
                    RoundNotFound -> Error.RoundNotFound
                    SetNotFound -> Error.SetNotFound
                }
            }
    }

    data class Result(
        val round: Round,
        val roundSet: RoundSet,
    )

    sealed class Error {
        object RoundNotFound : Error()
        object SetNotFound : Error()
    }
}