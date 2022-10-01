package io.github.staakk.progresstracker.domain.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource
) {

    suspend operator fun invoke(round: Round): Option<Round> = trainingDataSource.deleteRound(round)
}