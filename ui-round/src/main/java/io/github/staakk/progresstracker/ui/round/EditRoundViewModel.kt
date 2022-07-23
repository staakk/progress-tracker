package io.github.staakk.progresstracker.ui.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.training.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditRoundViewModel @Inject constructor(
    private val getExercises: GetExercises,
    private val observeRound: ObserveRound,
    private val updateRound: UpdateRound,
    private val deleteRound: DeleteRound,
    private val createSet: CreateSet,
) : ViewModel() {

    private val _state = MutableStateFlow<EditRoundState>(EditRoundState.Loading)

    val state: StateFlow<EditRoundState> = _state

    fun dispatch(event: EditRoundEvent) {
        when (event) {
            is EditRoundEvent.ScreenOpened -> onScreenOpened(event.roundId)
            is EditRoundEvent.UpdateExercise -> onExerciseUpdated(event.exercise)
            is EditRoundEvent.CreateSet -> onCreateSet()
            is EditRoundEvent.NewSetIdConsumed -> onNewSetIdConsumed()
            is EditRoundEvent.OpenDeleteDialog -> updateDialogState(DialogState.Open)
            is EditRoundEvent.CloseDeleteDialog -> updateDialogState(DialogState.Closed)
            is EditRoundEvent.DeleteRound -> onDeleteRound()
            is EditRoundEvent.DeleteRoundConsumed -> onDeleteRoundConsumed()
        }
    }

    private fun onDeleteRoundConsumed() {
        _state.update { EditRoundState.Loading }
    }

    private fun onCreateSet() {
        Timber.d("Create new set")
        viewModelScope.launch {
            _state.value
                .roundOrNull()
                ?.coLet(createSet)
                ?.fold(
                    { Timber.e(it.toString()) },
                    { (_, set) ->
                        _state.update { state ->
                            if (state is EditRoundState.Loaded) state.copy(newSetId = set.id)
                            else state
                        }
                    }
                )
        }
    }

    private fun onExerciseUpdated(exercise: Exercise) {
        viewModelScope.launch {
            _state.value.let { state ->
                if (state !is EditRoundState.Loaded) return@launch
                updateRound(state.round.copy(exercise = exercise))
                    .fold(
                        {},
                        { round -> _state.update { state.copy(round = round) } }
                    )
            }
        }
    }

    private fun onScreenOpened(roundId: Id) {
        Timber.d("Load round with id $roundId")

        viewModelScope.launch {
            val exercises = getExercises()
            observeRound(roundId)
                .onEach { round ->
                    _state.update { EditRoundState.Loaded(round = round, exercises = exercises) }
                }
                .collect()
        }
    }

    private fun onDeleteRound() {
        viewModelScope.launch {
            _state.value
                .roundOrNull()
                ?.coLet(deleteRound)
                ?.fold(
                    {},
                    { _state.update { EditRoundState.RoundDeleted } }
                )
        }
    }

    private fun onNewSetIdConsumed() {
        _state.update {
            if (it is EditRoundState.Loaded) it.copy(newSetId = null)
            else it
        }
    }

    private fun updateDialogState(dialogState: DialogState) {
        _state.update {
            if (it is EditRoundState.Loaded) it.copy(deleteDialogState = dialogState)
            else it
        }
    }
}