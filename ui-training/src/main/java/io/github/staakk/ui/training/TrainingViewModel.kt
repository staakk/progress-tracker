package io.github.staakk.ui.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _state = MutableStateFlow<TrainingState>(TrainingState.Loading)
    val state: StateFlow<TrainingState> = _state

    fun dispatch(event: TrainingEvent) {
        when (event) {
            is TrainingEvent.LoadTraining -> onLoadTraining(event.id)
            is TrainingEvent.CreateRound -> onCreateRound()
            is TrainingEvent.OpenDeleteDialog -> updateDialogState(DialogState.Open)
            is TrainingEvent.CloseDeleteDialog -> updateDialogState(DialogState.Closed)
            is TrainingEvent.DeleteTraining -> onDeleteTraining()
            is TrainingEvent.DeleteTrainingConsumed -> onDeleteTrainingConsumed()
        }
    }

    private fun onDeleteTrainingConsumed() {
        _state.update { TrainingState.Loading }
    }

    private fun onDeleteTraining() {
        viewModelScope.launch {
            _state.value
                .getTrainingOrNull()
                ?.coLet(deleteTraining)
                ?.fold(
                    {},
                    { _state.update { TrainingState.TrainingDeleted } }
                )
        }
    }

    private fun updateDialogState(dialogState: DialogState) {
        _state.update {
            if (it is TrainingState.Loaded) it.copy(dialogState = dialogState)
            else it
        }
    }

    private fun onLoadTraining(id: Id) {
        observeTraining(id)
            .onEach { training ->
                _state.update { TrainingState.Loaded(training, DialogState.Closed) }
            }
            .launchIn(viewModelScope)
    }

    private fun onCreateRound() {
        val training = (state.value as? TrainingState.Loaded)?.training ?: return

        viewModelScope.launch {
            createRound(training)
                .fold(
                    {},
                    { (training, round) ->

                    }
                )
        }
    }
}