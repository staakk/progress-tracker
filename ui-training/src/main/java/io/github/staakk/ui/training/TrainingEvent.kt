package io.github.staakk.ui.training

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Round

sealed class TrainingEvent {
    data class LoadTraining(val id: Id) : TrainingEvent()

    data class EditRound(val round: Round) : TrainingEvent()

    object CreateRound : TrainingEvent()
}
