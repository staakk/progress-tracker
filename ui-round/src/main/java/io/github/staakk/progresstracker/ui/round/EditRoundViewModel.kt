package io.github.staakk.progresstracker.ui.round

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.training.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EditRoundViewModel @Inject constructor(
    private val getExercises: GetExercises,
    private val observeRound: ObserveRound,
    private val updateRound: UpdateRound,
    private val deleteRound: DeleteRound,
    private val createSet: CreateSet,
) : ViewModel() {

    private val _state = MutableStateFlow(EditRoundState())

    val state: StateFlow<EditRoundState> = _state

    fun dispatch(event: Event) = viewModelDispatch(event)

    private fun updateDialogState(dialogState: DialogState) {
        _state.update { it.copy(deleteDialogState = dialogState) }
    }

    sealed class Event(
        action: Action<EditRoundViewModel>
    ) : ViewModelEvent<EditRoundViewModel>(action) {
        data class ScreenOpened(val roundId: Id) : Event({
            val exercises = getExercises()
            observeRound(roundId)
                .onEach { round ->
                    _state.update {
                        EditRoundState(
                            round = round,
                            exercises = exercises
                        )
                    }
                }
                .collect()
        })

        data class UpdateExercise(val exercise: Exercise) : Event({
            _state.value.let { state ->
                if (state.round == null) return@let
                updateRound(state.round.copy(exercise = exercise))
                    .fold(
                        {},
                        { round -> _state.update { state.copy(round = round) } }
                    )
            }
        })

        object CreateSet : Event({
            _state.value.round
                ?.coLet(createSet::invoke)
                ?.fold(
                    { },
                    { (_, set) ->
                        _state.update { state -> state.copy(newSetId = set.id) }
                    }
                )
        })

        object NewSetIdConsumed : Event({ _state.update { it.copy(newSetId = null) } })

        object OpenDeleteDialog : Event({ updateDialogState(DialogState.Open) })

        object CloseDeleteDialog : Event({ updateDialogState(DialogState.Closed) })

        object DeleteRound : Event({
            _state.value.round
                ?.coLet(deleteRound::invoke)
                ?.fold(
                    {},
                    { _state.update { it.copy(roundDeleted = true) } }
                )
        })

        object DeleteRoundConsumed : Event({ _state.update { EditRoundState() } })
    }
}