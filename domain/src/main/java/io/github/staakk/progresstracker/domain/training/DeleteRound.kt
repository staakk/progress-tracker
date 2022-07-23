package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource
): suspend (Round) -> Either<DeleteRound.Error, Round> {

    override suspend fun invoke(round: Round): Either<Error, Round> {
        return trainingDataSource.deleteRound(round)
            .mapLeft { Error.DeleteRoundError }
    }

    sealed class Error {
        object DeleteRoundError: Error()
    }
}