package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTraining @Inject constructor(
    private val trainingDataSource: TrainingDataSource
) : (Id) -> Flow<Training> {

    override fun invoke(id: Id): Flow<Training> = trainingDataSource.observeTraining(id)
}