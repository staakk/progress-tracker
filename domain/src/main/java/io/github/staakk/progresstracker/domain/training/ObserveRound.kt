package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    operator fun invoke(id: Id): Flow<Round> = trainingDataSource.observeRound(id)
}