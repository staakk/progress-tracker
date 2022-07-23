package io.github.staakk.progresstracker.ui.set

import io.github.staakk.progresstracker.data.training.RoundSet

sealed class EditSetState {

    object Loading : EditSetState()

    data class Loaded(
        val set: RoundSet,
        val deleteDialogState: DialogState = DialogState.Closed,
    ) : EditSetState()

    object SetDeleted : EditSetState()

    object SetUpdated : EditSetState()

    fun setOrNull() =
        if (this is Loaded) set
        else null

    fun isDeleteDialogOpen() =
        if (this is Loaded) deleteDialogState == DialogState.Open
        else false
}

enum class DialogState {
    Open, Closed
}