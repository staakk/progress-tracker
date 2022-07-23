package io.github.staakk.ui.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.domain.training.DeleteTraining
import io.github.staakk.progresstracker.domain.training.ObserveTrainings
import io.github.staakk.progresstracker.domain.training.SaveTraining
import io.github.staakk.ui.trainings.TrainingsEvent.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    fun dispatch(event: TrainingsEvent) {
        when (event) {
            is ScreenOpened -> onScreenOpened()
            is CreateNewTraining -> onCreateNewTraining()
            is TrainingsEvent.DeleteTraining -> onDeleteTraining(event.training)
            is NewTrainingIdConsumed -> onNewTrainingIdConsumed()
        }
    }

    private fun onNewTrainingIdConsumed() {
        _trainings.update { it.copy(newTrainingId = null) }
    }

    private fun onScreenOpened() {
        observeTrainings(ObserveTrainings.Criteria.All)
            .onEach { trainings -> _trainings.update { it.copy(trainings = trainings) } }
            .launchIn(viewModelScope)
    }

    private fun onCreateNewTraining() {
        viewModelScope.launch {
            saveTraining(Training(date = LocalDateTime.now()))
                .fold(
                    {},
                    { training ->
                        _trainings.update {
                            it.copy(newTrainingId = training.id)
                        }
                    }
                )
        }
    }

    private fun onDeleteTraining(training: Training) {
        viewModelScope.launch {
            deleteTraining(training)
        }
    }
}