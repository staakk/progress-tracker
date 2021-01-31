package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.data.round.SetDataSource
import javax.inject.Inject

class UpdateSet @Inject constructor(
    private val setDataSource: SetDataSource
) {

    fun invoke(
        round: Round,
        set: Set,
        reps: Int? = null,
        weight: Int? = null,
    ): Result {
        val updatedSet = setDataSource.update(
            set.copy(
                reps = reps ?: set.reps,
                weight = weight ?: set.weight,
            ),
            round.id
        )
        val updatedSets = round.sets.filter { it.id != set.id } + updatedSet
        val updatedRound = round.copy(sets = updatedSets)
        return Result(
            updatedRound,
            updatedSet
        )
    }

    data class Result(
        val round: Round,
        val set: Set
    )
}