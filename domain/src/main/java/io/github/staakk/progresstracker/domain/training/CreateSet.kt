package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (Round) -> Either<CreateSet.Error, Pair<Round, RoundSet>> {

    override suspend fun invoke(round: Round): Either<Error, Pair<Round, RoundSet>> {
        val newSet = RoundSet(
            ordinal = round.roundSets.nextOrdinal(),
            reps = 0,
            weight = 0,
        )
        val updatedRound = round.copy(roundSets = round.roundSets + newSet)
        return trainingDataSource.saveRound(updatedRound)
            .mapLeft { Error.CreateSetError }
            .map { saved -> saved to saved.roundSets.first { it.ordinal == newSet.ordinal } }
    }

    sealed class Error {
        object CreateSetError : Error()
    }

    private fun List<RoundSet>.nextOrdinal() = maxOfOrNull { it.ordinal }
        ?.let(Int::inc)
        ?: 0
}