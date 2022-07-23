package io.github.staakk.ui.training

import io.github.staakk.progresstracker.data.Id

sealed class TrainingEvent {
    data class LoadTraining(val id: Id) : TrainingEvent()

    object CreateRound : TrainingEvent()

    object NewRoundIdConsumed : TrainingEvent()

    object CloseDeleteDialog : TrainingEvent()

    object OpenDeleteDialog : TrainingEvent()

    object DeleteTraining : TrainingEvent()

    object DeleteTrainingConsumed : TrainingEvent()
}
