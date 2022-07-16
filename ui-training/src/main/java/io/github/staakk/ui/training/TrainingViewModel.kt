package io.github.staakk.ui.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.CreateRound
import io.github.staakk.progresstracker.domain.training.ObserveTraining
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val observeTraining: ObserveTraining,
    private val createRound: CreateRound,
) : ViewModel() {

    private val _state = MutableStateFlow<TrainingState>(TrainingState.Loading)
    val state: StateFlow<TrainingState> = _state

    fun dispatch(event: TrainingEvent) {
        when (event) {
            is TrainingEvent.LoadTraining -> onLoadTraining(event.id)
            is TrainingEvent.CreateRound -> onCreateRound()
        }
    }

    private fun onLoadTraining(id: Id) {
        observeTraining(id)
            .onEach {
                _state.value = TrainingState.Loaded(it)
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