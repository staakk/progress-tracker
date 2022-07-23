package io.github.staakk.ui.trainings

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training

data class TrainingsState(
    val newTrainingId: Id? = null,
    val trainings: List<Training> = listOf(),
)