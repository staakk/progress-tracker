package io.github.staakk.progresstracker.ui.exercise

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExercisesListViewModel @Inject constructor(
    private val getExercises: GetExercises,
) : ViewModel() {

    private val _searchValue = MutableLiveData("")

    private val _exercises = switchMap(_searchValue) { searchValue ->
        liveData(context = viewModelScope.coroutineContext) {
            wrapIdlingResource {
                val result = withContext(Dispatchers.IO) {
                    getExercises()
                        .filter { it.name.contains(searchValue, ignoreCase = true) }
                        .sortedBy { it.name }
                }
                emit(result)
            }
        }
    }

    val exercises: LiveData<List<Exercise>> = _exercises

    fun getSearchValue() = _searchValue.value!!

    fun setSearchValue(searchValue: String) {
        Timber.d("Search value = $searchValue")
        _searchValue.value = searchValue
    }
}