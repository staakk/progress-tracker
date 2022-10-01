package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    suspend operator fun invoke(roundSet: RoundSet): Option<RoundSet> =
        trainingDataSource.deleteSet(roundSet)
}