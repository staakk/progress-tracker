package io.github.staakk.progresstracker.ui.exercise

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExerciseById
import io.github.staakk.progresstracker.domain.exercise.SaveExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class EditExerciseViewModel @Inject constructor(
    private val getExerciseById: GetExerciseById,
    private val saveExercise: SaveExercise,
) : ViewModel() {

    private val mutableState = MutableStateFlow<EditExerciseState>(EditExerciseState.Loading)

    val state: StateFlow<EditExerciseState> = mutableState

    fun dispatch(event: Event) = viewModelDispatch(event)

    sealed class Event(
        action: Action<EditExerciseViewModel>
    ) : ViewModelEvent<EditExerciseViewModel>(action) {
        data class ScreenOpened(val exerciseId: Id?) : Event({
            mutableState.value =
                if (exerciseId == null) EditExerciseState.Editing(true, Exercise(name = ""))
                else wrapIdlingResource {
                    getExerciseById(exerciseId).fold(
                        { EditExerciseState.Error },
                        { EditExerciseState.Editing(false, it) }
                    )
                }
        })

        data class ExerciseNameChanged(val name: String) : Event({
            mutableState.update {
                if (it is EditExerciseState.Editing && it.exercise.name != name) {
                    it.copy(exercise = it.exercise.copy(name = name))
                } else it
            }
        })

        object SaveExerciseClicked : Event({
            mutableState.value.let {
                if (it !is EditExerciseState.Editing) return@let
                mutableState.value = EditExerciseState.Saving
                mutableState.value = wrapIdlingResource {
                    saveExercise(it.exercise)
                        .fold({ EditExerciseState.Error }, { EditExerciseState.Saved })
                }
            }
        })
    }
}