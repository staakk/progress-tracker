package io.github.staakk.progresstracker.ui.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise

sealed class EditExerciseState {

    object Loading : EditExerciseState()

    data class Editing(
        val isNewExercise: Boolean,
        val exercise: Exercise,
    ) : EditExerciseState()

    object Saving : EditExerciseState()

    object Saved : EditExerciseState()

    object Error : EditExerciseState()
}