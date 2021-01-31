package io.github.staakk.progresstracker.ui.journal.set

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.round.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class EditRoundViewModel @ViewModelInject constructor(
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

    private val _round = MutableLiveData<Round>(null)

    private val _roundDeleted = MutableLiveData(false)

    val round: LiveData<Round?> = _round

    val roundDeleted: LiveData<Boolean> = _roundDeleted

    fun loadRound(roundId: String) {
        Timber.d("Load round with id $roundId")

        _roundDeleted.value = false
        viewModelScope.launch {
            loadExercises()
            val result = withContext(Dispatchers.IO) {
                getRoundById(roundId)
            }
            result.fold(
                {},
                { _round.value = it }
            )
        }
    }

    fun createNewRound(createdAt: LocalDate) {
        Timber.d("Create new round with date ${createdAt.format(DateTimeFormatter.BASIC_ISO_DATE)}")

        _roundDeleted.value = false
        viewModelScope.launch {
            loadExercises()
            _round.value = withContext(Dispatchers.IO) {
                createRound(createdAt, _exercises.value!![0])
            }
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            _round.value = withContext(Dispatchers.IO) {
                updateRound(_round.value!!, exercise = exercise)
            }
        }
    }

    fun updateSet(set: Set) {
        viewModelScope.launch {
            _round.value = withContext(Dispatchers.IO) {
                updateSet.invoke(_round.value!!, set, set.reps, set.weight).round
            }
        }
    }

    fun createNewSet() {
        viewModelScope.launch {
            _round.value = withContext(Dispatchers.IO) {
                createSet(
                    _round.value!!,
                    Set(reps = 0,
                        weight = 0,
                        position = _round.value!!.sets.lastOrNull()?.position?.plus(1) ?: 0))
            }
        }
    }

    fun deleteSet(set: Set) {
        viewModelScope.launch {
            _round.value = withContext(Dispatchers.IO) {
                deleteSet.invoke(_round.value!!, set)
            }
        }
    }

    fun deleteCurrentRound() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRound(_round.value!!)
            }
            _round.value = null
            _roundDeleted.value = true
        }
    }

    private suspend fun loadExercises() {
        _exercises.value = withContext(Dispatchers.IO) { getExercises() }
    }

}