package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class UpdateSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    suspend fun invoke(set: RoundSet): Option<RoundSet> =
        trainingDataSource.saveSet(set)
}