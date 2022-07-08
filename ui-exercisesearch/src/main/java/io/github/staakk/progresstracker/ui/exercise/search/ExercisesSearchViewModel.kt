package io.github.staakk.progresstracker.ui.exercise.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.FindExerciseByName
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ExercisesSearchViewModel @Inject constructor(
    findExerciseByName: FindExerciseByName,
) : ViewModel() {

    private val _searchValue = MutableStateFlow("")

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())

    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        _searchValue
            .flatMapLatest(findExerciseByName)
            .onEach { _exercises.value = it }
            .launchIn(viewModelScope)
    }

    fun getSearchValue() = _searchValue.value

    fun setSearchValue(searchValue: String) {
        _searchValue.value = searchValue
    }
}