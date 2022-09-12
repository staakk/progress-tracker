package io.github.staakk.ui.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.CreateRound
import io.github.staakk.progresstracker.domain.training.DeleteTraining
import io.github.staakk.progresstracker.domain.training.ObserveTraining
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val observeTraining: ObserveTraining,
    private val createRound: CreateRound,
    private val deleteTraining: DeleteTraining,
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingState())
    val state: StateFlow<TrainingState> = _state

    fun dispatch(event: TrainingEvent) {
        when (event) {
            is TrainingEvent.LoadTraining -> onLoadTraining(event.id)
            is TrainingEvent.CreateRound -> onCreateRound()
            is TrainingEvent.NewRoundIdConsumed -> onNewRoundIdConsumed()
            is TrainingEvent.OpenDeleteDialog -> updateDialogState(DialogState.Open)
            is TrainingEvent.CloseDeleteDialog -> updateDialogState(DialogState.Closed)
            is TrainingEvent.DeleteTraining -> onDeleteTraining()
            is TrainingEvent.DeleteTrainingConsumed -> onDeleteTrainingConsumed()
        }
    }

    private fun onNewRoundIdConsumed() {
        _state.update { it.copy(newRoundId = null) }
    }

    private fun onDeleteTrainingConsumed() {
        _state.update {
            it.copy(
                training = null,
                trainingDeleted = false,
                dialogState = DialogState.Closed
            )
        }
    }

    private fun onDeleteTraining() {
        viewModelScope.launch {
            _state.value
                .training
                ?.coLet(deleteTraining)
                ?.fold(
                    {},
                    { _state.update { it.copy(trainingDeleted = true) } }
                )
        }
    }

    private fun updateDialogState(dialogState: DialogState) {
        _state.update { it.copy(dialogState = dialogState) }
    }

    private fun onLoadTraining(id: Id) {
        observeTraining(id)
            .onEach { training -> _state.update { it.copy(training = training) } }
            .launchIn(viewModelScope)
    }

    private fun onCreateRound() {
        val training = state.value.training ?: return

        viewModelScope.launch {
            createRound(training)
                .fold(
                    {},
                    { (_, round) -> _state.update { it.copy(newRoundId = round.id) } }
                )
        }
    }
}