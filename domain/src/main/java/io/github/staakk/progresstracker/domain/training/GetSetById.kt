package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class GetSetById @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    suspend operator fun invoke(setId: Id): Option<RoundSet> = trainingDataSource.getSetById(setId)
}