package io.github.staakk.progresstracker.ui.round

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.training.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditRoundViewModel @Inject constructor(
    private val getExercises: GetExercises,
    private val observeRound: ObserveRound,
    private val updateRound: UpdateRound,
    private val createSet: CreateSet,
    private val updateSetUseCase: UpdateSet,
    private val deleteSetUseCase: DeleteSet,
) : ViewModel() {

    private val _exercises = MutableLiveData(emptyList<Exercise>())

    val exercises: LiveData<List<Exercise>> = _exercises

    private val _round = MutableLiveData<Round?>(null)

    val round: LiveData<Round?> = _round

    fun loadRound(roundId: Id) {
        Timber.d("Load round with id $roundId")

        viewModelScope.launch {
            loadExercises()
            observeRound(roundId)
                .onEach { _round.postValue(it) }
                .collect()
        }
    }

    fun updateExercise(exercise: Exercise) {
        Timber.d("Update exercise $exercise")
        viewModelScope.launch {
            wrapIdlingResource {
                updateRound(_round.value!!.copy(exercise = exercise))
                    .fold({ Timber.e(it.toString()) }, {})
            }
        }
    }

    fun updateSet(roundSet: RoundSet) {
        // TODO: throttle saving changes
        Timber.d("Update set $roundSet")
        viewModelScope.launch {
            wrapIdlingResource {
                updateSetUseCase(roundSet)
                    .fold({ Timber.e(it.toString()) }, { })
            }
        }
    }

    fun createNewSet() {
        Timber.d("Create new set")
        viewModelScope.launch {
            wrapIdlingResource {
                createSet(_round.value!!)
                    .fold({ Timber.e(it.toString()) }, { })
            }
        }
    }

    fun deleteSet(roundSet: RoundSet) {
        Timber.d("Delete set $roundSet")
        viewModelScope.launch {
            wrapIdlingResource {
                deleteSetUseCase(roundSet)
                    .fold({ Timber.e(it.toString()) }, { })
            }
        }
    }

    private suspend fun loadExercises() {
        _exercises.value = getExercises()
    }
}