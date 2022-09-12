package io.github.staakk.progresstracker.ui.round

import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round

data class EditRoundState(
    val round: Round? = null,
    val exercises: List<Exercise> = emptyList(),
    val deleteDialogState: DialogState = DialogState.Closed,
    val newSetId: Id? = null,
    val roundDeleted: Boolean = false,
) {
    val roundSets get() = round?.roundSets ?: emptyList()

    val selectedExerciseIndex by lazy {
        if (round == null) -1
        else {
            exercises.indexOf(round.exercise)
        }
    }
}