package io.github.staakk.progresstracker.ui.exercise

import io.github.staakk.progresstracker.data.Id

sealed class EditExerciseEvent {
    data class ScreenOpened(val exerciseId: Id?) : EditExerciseEvent()
    data class ExerciseNameChanged(val name: String) : EditExerciseEvent()
    object SaveExerciseClicked : EditExerciseEvent()
}
