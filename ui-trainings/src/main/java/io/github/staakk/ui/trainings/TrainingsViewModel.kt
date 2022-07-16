package io.github.staakk.ui.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.domain.training.DeleteTraining
import io.github.staakk.progresstracker.domain.training.ObserveTrainings
import io.github.staakk.progresstracker.domain.training.SaveTraining
import io.github.staakk.ui.trainings.TrainingsEvent.CreateNewTraining
import io.github.staakk.ui.trainings.TrainingsEvent.ScreenOpened
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val observeTrainings: ObserveTrainings,
    private val saveTraining: SaveTraining,
    private val deleteTraining: DeleteTraining
) : ViewModel() {

    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val trainings: StateFlow<List<Training>> = _trainings

    fun dispatch(event: TrainingsEvent) {
        when (event) {
            is ScreenOpened -> onScreenOpened()
            is CreateNewTraining -> onCreateNewTraining()
            is TrainingsEvent.DeleteTraining -> onDeleteTraining(event.training)
            else -> Unit
        }
    }

    private fun onScreenOpened() {
        observeTrainings(ObserveTrainings.Criteria.All)
            .onEach { _trainings.value = it }
            .launchIn(viewModelScope)
    }

    private fun onCreateNewTraining() {
        viewModelScope.launch {
            saveTraining(Training(date = LocalDateTime.now()))
        }
    }

    private fun onDeleteTraining(training: Training) {
        viewModelScope.launch {
            deleteTraining(training)
        }
    }
}