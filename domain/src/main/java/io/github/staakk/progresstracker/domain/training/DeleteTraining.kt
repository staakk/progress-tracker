package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteTraining @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
): suspend (Training) -> Either<DeleteTraining.Error, Training> {

    override suspend fun invoke(training: Training): Either<Error, Training> {
        return trainingDataSource.deleteTraining(training)
            .mapLeft { Error.DeleteTrainingError }
    }

    sealed class Error {
        object DeleteTrainingError: Error()
    }
}