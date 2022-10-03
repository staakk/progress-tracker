package io.github.staakk.ui.trainings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.viewmodel.Action
import io.github.staakk.progresstracker.common.android.viewmodel.ViewModelEvent
import io.github.staakk.progresstracker.common.android.viewmodel.viewModelDispatch
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.domain.training.DeleteTraining
import io.github.staakk.progresstracker.domain.training.ObserveTrainings
import io.github.staakk.progresstracker.domain.training.SaveTraining
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val observeTrainings: ObserveTrainings,
    private val saveTraining: SaveTraining,
    private val deleteTraining: DeleteTraining,
) : ViewModel() {

    private val _trainings = MutableStateFlow(TrainingsState())
    val trainings: StateFlow<TrainingsState> = _trainings

    fun dispatch(event: Event) = viewModelDispatch(event)

    sealed class Event(
        action: Action<TrainingsViewModel>,
    ) : ViewModelEvent<TrainingsViewModel>(action) {

        object ScreenOpened : Event({
            observeTrainings(ObserveTrainings.Criteria.All)
                .onEach { trainings -> _trainings.update { it.copy(trainings = trainings) } }
                .collect()
        })

        object CreateNewTraining : Event({
            saveTraining(Training(date = LocalDateTime.now()))
                .fold(
                    {},
                    { training ->
                        _trainings.update {
                            it.copy(newTrainingId = training.id)
                        }
                    }
                )
        })

        data class DeleteTraining(val training: Training) : Event({ deleteTraining(training) })

        object NewTrainingIdConsumed : Event({
            _trainings.update { it.copy(newTrainingId = null) }
        })
    }
}