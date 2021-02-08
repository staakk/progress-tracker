package io.github.staakk.progresstracker.ui.exercise

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.util.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExercisesListViewModel @Inject constructor(
    private val getExercises: GetExercises,
) : ViewModel() {

    private val _searchValue = MutableLiveData("")

    private val _exercises = switchMap(_searchValue) { searchValue ->
        liveData(context = viewModelScope.coroutineContext) {
            val result = withContext(Dispatchers.IO) {
                getExercises()
                    .filter { it.name.contains(searchValue, ignoreCase = true) }
                    .sortedBy { it.name }
            }
            emit(result)
            EspressoIdlingResource.decrement()
        }
    }

    val exercises: LiveData<List<Exercise>> = _exercises

    fun getSearchValue() = _searchValue.value!!

    fun setSearchValue(searchValue: String) {
        EspressoIdlingResource.increment()
        _searchValue.value = searchValue
    }
}