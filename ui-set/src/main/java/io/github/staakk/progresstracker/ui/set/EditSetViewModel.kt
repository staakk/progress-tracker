package io.github.staakk.progresstracker.ui.set

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.DeleteSet
import io.github.staakk.progresstracker.domain.training.GetSetById
import io.github.staakk.progresstracker.domain.training.UpdateSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditSetViewModel @Inject constructor(
    private val getSetById: GetSetById,
    private val updateSet: UpdateSet,
    private val deleteSet: DeleteSet,
) : ViewModel() {

    private val _state = MutableStateFlow<EditSetState>(EditSetState.Loading)

    val state: StateFlow<EditSetState> = _state

    fun dispatch(event: Event) = viewModelDispatch(event)

    private fun updateDialogState(dialogState: DialogState) {
        _state.update {
            if (it is EditSetState.Loaded) it.copy(deleteDialogState = dialogState)
            else it
        }
    }

    sealed class Event(
        action: Action<EditSetViewModel>,
    ) : ViewModelEvent<EditSetViewModel>(action) {

        data class ScreenOpened(val setId: Id) : Event({
            getSetById(setId)
                .fold(
                    {},
                    { set -> _state.update { EditSetState.Loaded(set) } }
                )
        })

        data class SaveSet(
            val reps: String,
            val weight: String,
        ) : Event(action@{
            val state = _state.value
            if (state !is EditSetState.Loaded) return@action
            state
                .set
                .copy(reps = reps.toIntOrNull() ?: 0, weight = weight.toIntOrNull() ?: 0)
                .coLet(updateSet::invoke)
                .fold(
                    {},
                    { _state.update { EditSetState.SetUpdated } }
                )
        })

        object OpenDeleteDialog : Event({ updateDialogState(DialogState.Open) })

        object CloseDeleteDialog : Event({ updateDialogState(DialogState.Closed) })

        object DeleteSet : Event({
            _state.value
                .setOrNull()
                ?.coLet(deleteSet::invoke)
                ?.fold(
                    {},
                    { _state.update { EditSetState.SetDeleted } }
                )
        })

        object TerminalEventConsumed : Event({ _state.update { EditSetState.Loading } })
    }
}