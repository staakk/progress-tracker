package io.github.staakk.progresstracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExerciseById
import io.github.staakk.progresstracker.domain.exercise.SaveExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditExerciseViewModel @Inject constructor(
    private val getExerciseById: GetExerciseById,
    private val saveExercise: SaveExercise,
) : ViewModel() {

    private val _state = MutableStateFlow<EditExerciseState>(EditExerciseState.Loading)

    val state: StateFlow<EditExerciseState> = _state

    fun dispatch(event: EditExerciseEvent) {
        when (event) {
            is EditExerciseEvent.ScreenOpened -> onScreenOpened(event.exerciseId)
            is EditExerciseEvent.ExerciseNameChanged -> onExerciseNameChanged(event.name)
            EditExerciseEvent.SaveExerciseClicked -> onSaveExercise()
        }
    }

    private fun onScreenOpened(exerciseId: String?) {
        _state.value = EditExerciseState.Loading
        viewModelScope.launch {
            _state.value =
                if (exerciseId == null) EditExerciseState.Editing(true, Exercise(name = ""))
                else wrapIdlingResource {
                    getExerciseById(exerciseId).fold(
                        { EditExerciseState.Error },
                        { EditExerciseState.Editing(false, it) }
                    )
                }
        }
    }

    private fun onExerciseNameChanged(name: String) {
        _state.value.let {
            if (it is EditExerciseState.Editing && it.exercise.name != name) {
                _state.value = it.copy(exercise = it.exercise.copy(name = name))
            }
        }
    }

    private fun onSaveExercise() {
        _state.value.let {
            if (it !is EditExerciseState.Editing) return
            _state.value = EditExerciseState.Saving
            viewModelScope.launch {
                _state.value = wrapIdlingResource {
                    saveExercise(it.exercise)
                        .fold({ EditExerciseState.Error }, { EditExerciseState.Saved })
                }
            }
        }
    }
}