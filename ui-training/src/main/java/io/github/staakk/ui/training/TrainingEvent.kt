package io.github.staakk.ui.training

import io.github.staakk.progresstracker.common.time.TimeOfDay
import io.github.staakk.progresstracker.data.Id
import java.time.LocalDate

sealed class TrainingEvent {
    data class LoadTraining(val id: Id) : TrainingEvent()

    object CreateRound : TrainingEvent()

    object NewRoundIdConsumed : TrainingEvent()

    object CloseDeleteDialog : TrainingEvent()

    object OpenDeleteDialog : TrainingEvent()

    object DeleteTraining : TrainingEvent()

    object DeleteTrainingConsumed : TrainingEvent()

    data class UpdateTrainingDate(val date: LocalDate): TrainingEvent()

    data class UpdateTrainingTime(val time: TimeOfDay): TrainingEvent()
}
