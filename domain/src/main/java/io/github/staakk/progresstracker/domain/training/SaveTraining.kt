package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class SaveTraining @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
): suspend (Training) -> Either<SaveTraining.Error, Training> {

    override suspend operator fun invoke(training: Training): Either<Error, Training> {
        return trainingDataSource.saveTraining(training)
            .mapLeft { Error.SaveTrainingError }
    }

    sealed class Error {
        object SaveTrainingError: Error()
    }
}