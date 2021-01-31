package io.github.staakk.progresstracker.ui.exercise

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import kotlinx.coroutines.Dispatchers

class ExercisesListViewModel @ViewModelInject constructor(
    private val getExercises: GetExercises
): ViewModel() {

    private val _searchValue = MutableLiveData("")

    private val _exercises = switchMap(_searchValue) { searchValue ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(
                getExercises()
                    .filter { it.name.contains(searchValue, ignoreCase = true) }
                    .sortedBy { it.name }
            )
        }
    }

    val exercises: LiveData<List<Exercise>> = _exercises

    fun getSearchValue() = _searchValue.value!!

    fun setSearchValue(searchValue: String) {
        _searchValue.value = searchValue
    }
}