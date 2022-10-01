package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class SaveTraining @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    suspend operator fun invoke(training: Training): Option<Training> =
        trainingDataSource.saveTraining(training)
}