package io.github.staakk.ui.trainings

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training
import java.time.LocalDate

data class TrainingsState(
    val newTrainingId: Id? = null,
    val trainings: List<Training> = listOf(),
    val exerciseQuery: String? = null,
    val dateQuery: Pair<LocalDate, LocalDate>? = null,
)