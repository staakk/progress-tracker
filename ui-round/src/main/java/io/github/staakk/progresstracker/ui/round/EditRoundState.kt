package io.github.staakk.progresstracker.ui.round

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round

sealed class EditRoundState {

    object Loading : EditRoundState()

    data class Loaded(
        val round: Round,
        val exercises: List<Exercise> = emptyList(),
        val deleteDialogState: DialogState = DialogState.Closed,
        val newSetId: Id? = null,
    ) : EditRoundState()

    object RoundDeleted : EditRoundState()

    fun roundOrNull() =
        if (this is Loaded) round
        else null

    fun exercisesOrEmpty() =
        if (this is Loaded) exercises
        else emptyList()

    fun isDeleteDialogOpen() =
        if (this is Loaded) deleteDialogState == DialogState.Open
        else false
}

enum class DialogState {
    Open, Closed
}