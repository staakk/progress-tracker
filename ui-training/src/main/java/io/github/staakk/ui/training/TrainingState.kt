package io.github.staakk.ui.training

import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training

data class TrainingState(
    val training: Training? = null,
    val dialogState: DialogState = DialogState.Closed,
    val newRoundId: Id? = null,
    val trainingDeleted: Boolean = false,
) {
    fun isLoading() = training == null
}