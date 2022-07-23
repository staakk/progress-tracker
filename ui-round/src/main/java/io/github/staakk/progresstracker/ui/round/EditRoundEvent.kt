package io.github.staakk.progresstracker.ui.round

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise

sealed class EditRoundEvent {

    data class ScreenOpened(val roundId: Id) : EditRoundEvent()

    data class UpdateExercise(val exercise: Exercise) : EditRoundEvent()

    object CreateSet : EditRoundEvent()

    object NewSetIdConsumed : EditRoundEvent()

    object OpenDeleteDialog : EditRoundEvent()

    object CloseDeleteDialog : EditRoundEvent()

    object DeleteRound : EditRoundEvent()

    object DeleteRoundConsumed : EditRoundEvent()
}