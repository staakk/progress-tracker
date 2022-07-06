package io.github.staakk.progresstracker.ui.exercise

sealed class EditExerciseEvent {
    data class ScreenOpened(val exerciseId: String?) : EditExerciseEvent()
    data class ExerciseNameChanged(val name: String) : EditExerciseEvent()
    object SaveExerciseClicked : EditExerciseEvent()
}
