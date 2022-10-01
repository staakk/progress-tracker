package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    suspend fun invoke(round: Round): Option<Pair<Round, RoundSet>> {
        val newSet = RoundSet(
            ordinal = round.roundSets.nextOrdinal(),
            reps = 0,
            weight = 0,
        )
        val updatedRound = round.copy(roundSets = round.roundSets + newSet)
        return trainingDataSource.saveRound(updatedRound)
            .map { saved -> saved to saved.roundSets.first { it.ordinal == newSet.ordinal } }
    }

    private fun List<RoundSet>.nextOrdinal() = maxOfOrNull { it.ordinal }
        ?.let(Int::inc)
        ?: 0
}