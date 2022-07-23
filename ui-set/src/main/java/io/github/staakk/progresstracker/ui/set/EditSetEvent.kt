package io.github.staakk.progresstracker.ui.set

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise

sealed class EditSetEvent {

    data class ScreenOpened(val setId: Id): EditSetEvent()

    data class SaveSet(
        val reps: String,
        val weight: String,
    ) : EditSetEvent()

    object OpenDeleteDialog : EditSetEvent()

    object CloseDeleteDialog : EditSetEvent()

    object DeleteSet : EditSetEvent()

    object TerminalEventConsumed : EditSetEvent()
}