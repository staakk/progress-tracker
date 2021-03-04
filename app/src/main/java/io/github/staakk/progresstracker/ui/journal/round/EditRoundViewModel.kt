package io.github.staakk.progresstracker.ui.journal.round

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.round.*
import io.github.staakk.progresstracker.util.wrapIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditRoundViewModel @Inject constructor(
    private val getExercises: GetExercises,
    private val getRoundById: GetRoundById,
    private val createRound: CreateRound,
    private val updateRound: UpdateRound,
    private val deleteRound: DeleteRound,
    private val updateSet: UpdateSet,
    private val createSet: CreateSet,
    private val deleteSet: DeleteSet,
) : ViewModel() {

    private val _exercises = MutableLiveData(emptyList<Exercise>())

    val exercises: LiveData<List<Exercise>> = _exercises

    private val _round = MutableLiveData<Round?>(null)

    private val _roundDeleted = MutableLiveData(false)

    val round: LiveData<Round?> = _round

    val roundDeleted: LiveData<Boolean> = _roundDeleted

    fun loadRound(roundId: String) {
        Timber.d("Load round with id $roundId")

        _roundDeleted.value = false
        viewModelScope.launch {
            wrapIdlingResource {
                loadExercises()
                val result = withContext(Dispatchers.IO) {
                    getRoundById(roundId)
                }
                result.fold(
                    { Timber.e(it.toString()) },
                    { _round.value = it }
                )
            }
        }
    }

    fun createNewRound(createdAt: LocalDate) {
        Timber.d("Create new round with date ${createdAt.format(DateTimeFormatter.ISO_DATE)}")

        _roundDeleted.value = false
        viewModelScope.launch {
            wrapIdlingResource {
                loadExercises()
                withContext(Dispatchers.IO) {
                    createRound(createdAt.atTime(LocalTime.now()),
                        _exercises.value!![0])
                }
                    .fold({ Timber.e(it.toString()) }, { _round.value = it })
            }
        }
    }

    fun updateExercise(exercise: Exercise) {
        Timber.d("Update exercise $exercise")
        viewModelScope.launch {
            wrapIdlingResource {
                withContext(Dispatchers.IO) { updateRound(_round.value!!, exercise = exercise) }
                    .fold({ Timber.e(it.toString()) }, { _round.value = it })
            }
        }
    }

    fun updateSet(roundSet: RoundSet) {
        Timber.d("Update set $roundSet")
        viewModelScope.launch {
            wrapIdlingResource {
                withContext(Dispatchers.IO) {
                    updateSet(_round.value!!,
                        roundSet,
                        reps = roundSet.reps,
                        weight = roundSet.weight)
                }.fold({ Timber.e(it.toString()) }, { _round.value = it.round })
            }
        }
    }

    fun createNewSet() {
        Timber.d("Create new set")
        viewModelScope.launch {
            wrapIdlingResource {
                withContext(Dispatchers.IO) {
                    createSet(
                        _round.value!!,
                        reps = 0,
                        weight = 0,
                        position = _round.value!!.roundSets.lastOrNull()?.position?.plus(1) ?: 0
                    )
                }.fold({ Timber.e(it.toString()) }, { _round.value = it })
            }
        }
    }

    fun deleteSet(roundSet: RoundSet) {
        Timber.d("Delete set $roundSet")
        viewModelScope.launch {
            wrapIdlingResource {
                withContext(Dispatchers.IO) { deleteSet.invoke(_round.value!!, roundSet) }
                    .fold({ Timber.e(it.toString()) }, { _round.value = it })
            }
        }
    }

    fun deleteCurrentRound() {
        Timber.d("Delete current round ${_round.value}")
        viewModelScope.launch {
            wrapIdlingResource {
                withContext(Dispatchers.IO) { deleteRound(_round.value!!) }
                    .fold({ Timber.e(it.toString()) }, {
                        _round.value = null
                        _roundDeleted.value = true
                    })
            }
        }
    }

    private suspend fun loadExercises() {
        _exercises.value = withContext(Dispatchers.IO) { getExercises() }
    }
}