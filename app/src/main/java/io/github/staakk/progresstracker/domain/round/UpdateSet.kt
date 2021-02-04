package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.UpdateSetError.RoundNotFound
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.UpdateSetError.SetNotFound
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Either
import javax.inject.Inject

class UpdateSet @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    fun invoke(
        round: Round,
        set: Set,
        reps: Int? = null,
        weight: Int? = null,
    ): Either<Error, Result> {
        return roundDataSource.updateSet(
            set.copy(
                reps = reps ?: set.reps,
                weight = weight ?: set.weight,
            ),
            round.id
        )
            .map { updatedSet ->
                val updatedSets = round.sets.filter { it.id != set.id } + updatedSet
                val updatedRound = round.copy(sets = updatedSets)
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
        val set: Set,
    )

    sealed class Error {
        object RoundNotFound : Error()
        object SetNotFound : Error()
    }
}