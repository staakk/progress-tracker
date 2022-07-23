package io.github.staakk.ui.training

import io.github.staakk.progresstracker.data.training.Training

sealed class TrainingState {
    object Loading : TrainingState()

    data class Loaded(
        val training: Training,
        val dialogState: DialogState,
    ) : TrainingState()

    object TrainingDeleted : TrainingState()

    fun getTrainingOrNull() =
        if (this is Loaded) training
        else null
}

sealed class DialogState {
    object Open : DialogState()
    object Closed : DialogState()
}