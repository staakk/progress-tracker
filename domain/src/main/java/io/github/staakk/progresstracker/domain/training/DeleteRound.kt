package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource
): suspend (Training, Round) -> Either<DeleteRound.Error, Training> {

    override suspend fun invoke(training: Training, round: Round): Either<Error, Training> {
        val updatedTraining = training.copy(rounds = training.rounds - round)
        return trainingDataSource.deleteRound(round)
            .mapLeft { Error.DeleteRoundError }
            .map { updatedTraining }
    }

    sealed class Error {
        object DeleteRoundError: Error()
    }
}