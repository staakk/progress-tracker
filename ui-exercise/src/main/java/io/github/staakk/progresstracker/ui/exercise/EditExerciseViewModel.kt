package io.github.staakk.progresstracker.ui.exercise

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.CreateExercise
import io.github.staakk.progresstracker.domain.exercise.GetExerciseById
import io.github.staakk.progresstracker.domain.exercise.UpdateExercise
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExerciseViewModel @Inject constructor(
    private val getExerciseById: GetExerciseById,
    private val createExercise: CreateExercise,
    private val updateExercise: UpdateExercise,
) : ViewModel() {

    private val exercise = MutableLiveData<Exercise?>()

    private val _exerciseName = MutableLiveData<String>()

    val exerciseName: LiveData<String> = _exerciseName

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Editing)

    val screenState: LiveData<ScreenState> = _screenState

    fun loadExercise(id: String?) {
        _screenState.value = ScreenState.Editing
        _exerciseName.value = ""
        exercise.value = null

        if (id == null) return
        viewModelScope.launch {
            wrapIdlingResource {
                getExerciseById(id).fold(
                    { _screenState.value = ScreenState.Error(ErrorType.UnknownExerciseId) },
                    {
                        exercise.value = it
                        _exerciseName.value = it.name
                    }
                )
            }
        }
    }

    fun setExerciseName(name: String) {
        _exerciseName.value = name
    }

    fun saveExercise() {
        _screenState.value = ScreenState.Saving
        viewModelScope.launch {
            wrapIdlingResource {
                val name = _exerciseName.value!!
                exercise.value
                    ?.let {
                        _screenState.value = updateExercise(it, name).fold(
                            { ScreenState.Error(ErrorType.NameAlreadyExists) },
                            { ScreenState.Saved }
                        )
                    }
                    ?: run {
                        _screenState.value = createExercise(name).fold(
                            { ScreenState.Error(ErrorType.NameAlreadyExists) },
                            { ScreenState.Saved }
                        )
                    }
            }
        }
    }

    sealed class ScreenState(val isInteractive: Boolean) {
        object Editing : ScreenState(true)
        object Saving : ScreenState(false)
        data class Error(val type: ErrorType) : ScreenState(true)
        object Saved : ScreenState(false)
    }

    sealed class ErrorType {
        object NameAlreadyExists : ErrorType()
        object UnknownExerciseId : ErrorType()
    }
}