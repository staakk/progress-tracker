package io.github.staakk.ui.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.domain.training.ObserveTrainings
import io.github.staakk.progresstracker.domain.training.SaveTraining
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val observeTrainings: ObserveTrainings,
    private val saveTraining: SaveTraining,
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingsState())
    val state: StateFlow<TrainingsState> = _state
    var observeTrainingsJob: Job? = null

    fun dispatch(event: Event) = viewModelDispatch(event)

    private fun observeTrainingsWithStateCriteria() {
        observeTrainingsJob?.cancel()
        observeTrainingsJob = viewModelScope.launch {
            observeTrainings(
                ObserveTrainings.Criteria(
                    exerciseQuery = _state.value.exerciseQuery.orEmpty(),
                    fromDate = (_state.value.dateQuery?.first ?: LocalDate.MIN).atStartOfDay(),
                    toDate = ((_state.value.dateQuery?.second
                        ?: LocalDate.MAX).atTime(LocalTime.MAX))
                )
            )
                .onEach { trainings -> _state.update { it.copy(trainings = trainings) } }
                .collect()
        }
    }

    sealed class Event(
        action: Action<TrainingsViewModel>,
    ) : ViewModelEvent<TrainingsViewModel>(action) {

        object ScreenOpened : Event({
            observeTrainingsWithStateCriteria()
        })

        object CreateNewTraining : Event({
            saveTraining(Training(date = LocalDateTime.now()))
                .fold(
                    {},
                    { training ->
                        _state.update { it.copy(newTrainingId = training.id) }
                    }
                )
        })

        object NewTrainingIdConsumed : Event({
            _state.update { it.copy(newTrainingId = null) }
        })

        data class ExerciseQueryChanged(val newQuery: String) : Event({
            _state.update { it.copy(exerciseQuery = newQuery) }
            observeTrainingsWithStateCriteria()
        })

        data class DateQueryChanged(val newQuery: Pair<LocalDate, LocalDate>) : Event({
            _state.update { it.copy(dateQuery = newQuery) }
            observeTrainingsWithStateCriteria()
        })
    }
}