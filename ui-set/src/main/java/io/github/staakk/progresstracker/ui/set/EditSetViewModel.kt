package io.github.staakk.progresstracker.ui.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditSetViewModel @Inject constructor(
    private val getSetById: GetSetById,
    private val updateSet: UpdateSet,
    private val deleteSet: DeleteSet,
) : ViewModel() {

    private val _state = MutableStateFlow<EditSetState>(EditSetState.Loading)

    val state: StateFlow<EditSetState> = _state

    fun dispatch(event: EditSetEvent) {
        when (event) {
            is EditSetEvent.ScreenOpened -> onScreenOpened(event.setId)
            is EditSetEvent.SaveSet -> onSaveSet(event.reps, event.weight)
            is EditSetEvent.DeleteSet -> onDeleteSet()
            is EditSetEvent.TerminalEventConsumed -> onDeleteSetConsumed()
            is EditSetEvent.OpenDeleteDialog -> updateDialogState(DialogState.Open)
            is EditSetEvent.CloseDeleteDialog -> updateDialogState(DialogState.Closed)
        }
    }

    private fun onDeleteSetConsumed() {
        _state.update { EditSetState.Loading }
    }

    private fun onDeleteSet() {
        viewModelScope.launch {
            _state.value
                .setOrNull()
                ?.coLet(deleteSet)
                ?.fold(
                    {},
                    { _state.update { EditSetState.SetDeleted } }
                )
        }
    }

    private fun onSaveSet(reps: String, weight: String) {
        viewModelScope.launch {
            val state = _state.value
            if (state !is EditSetState.Loaded) return@launch
            state
                .set
                .copy(reps = reps.toIntOrNull() ?: 0, weight = weight.toIntOrNull() ?: 0)
                .coLet(updateSet)
                .fold(
                    {},
                    { _state.update { EditSetState.SetUpdated } }
                )
        }
    }

    private fun onScreenOpened(setId: Id) {
        Timber.d("Load round with id $setId")

        viewModelScope.launch {
            getSetById(setId)
                .fold(
                    {},
                    { set -> _state.update { EditSetState.Loaded(set) } }
                )
        }
    }

    private fun updateDialogState(dialogState: DialogState) {
        _state.update {
            if (it is EditSetState.Loaded) it.copy(deleteDialogState = dialogState)
            else it
        }
    }
}