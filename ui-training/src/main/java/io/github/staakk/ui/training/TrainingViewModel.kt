package io.github.staakk.ui.training

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.common.coroutines.coLet
import io.github.staakk.progresstracker.common.time.TimeOfDay
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.CreateRound
import io.github.staakk.progresstracker.domain.training.DeleteTraining
import io.github.staakk.progresstracker.domain.training.ObserveTraining
import io.github.staakk.progresstracker.domain.training.SaveTraining
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val observeTraining: ObserveTraining,
    private val createRound: CreateRound,
    private val deleteTraining: DeleteTraining,
    private val saveTraining: SaveTraining,
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingState())
    val state: StateFlow<TrainingState> = _state

    fun dispatch(event: Event) = viewModelDispatch(event)

    private fun updateDialogState(dialogState: DialogState) {
        _state.update { it.copy(dialogState = dialogState) }
    }

    sealed class Event(
        action: Action<TrainingViewModel>,
    ) : ViewModelEvent<TrainingViewModel>(action) {
        data class LoadTraining(val id: Id) : Event({
            observeTraining(id)
                .onEach { training -> _state.update { it.copy(training = training) } }
                .collect()
        })

        object CreateRound : Event(action@{
            val training = state.value.training ?: return@action
            createRound(training)
                .fold(
                    {},
                    { (_, round) -> _state.update { it.copy(newRoundId = round.id) } }
                )
        })

        object NewRoundIdConsumed : Event({ _state.update { it.copy(newRoundId = null) } })

        object CloseDeleteDialog : Event({ updateDialogState(DialogState.Closed) })

        object OpenDeleteDialog : Event({ updateDialogState(DialogState.Open) })

        object DeleteTraining : Event({
            _state.value
                .training
                ?.coLet(deleteTraining::invoke)
                ?.fold(
                    {},
                    { _state.update { it.copy(trainingDeleted = true) } }
                )
        })

        object DeleteTrainingConsumed : Event({
            _state.update {
                it.copy(
                    training = null,
                    trainingDeleted = false,
                    dialogState = DialogState.Closed
                )
            }
        })

        data class UpdateTrainingDate(val date: LocalDate) : Event(action@{
            val training = state.value.training ?: return@action
            val updatedTraining = training.copy(
                date = training.date.with(date)
            )
            saveTraining(updatedTraining)
        })

        data class UpdateTrainingTime(val time: TimeOfDay) : Event(action@{
            val training = state.value.training ?: return@action

            val updatedTraining = training.copy(
                date = training.date
                    .withHour(time.hour)
                    .withMinute(time.minute)
            )
            saveTraining(updatedTraining)
        })
    }
}