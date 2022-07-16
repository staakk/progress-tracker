package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class UpdateRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (Round) -> Either<UpdateRound.Error, Round> {

    override suspend fun invoke(round: Round): Either<Error, Round> {
        return trainingDataSource.saveRound(round)
            .mapLeft { Error.UpdateRoundError }
    }

    sealed class Error {
        object UpdateRoundError: Error()
    }
}