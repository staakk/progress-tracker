package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FindExerciseByName @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
): suspend (String) -> Flow<List<Exercise>> {

    override suspend fun invoke(search: String): Flow<List<Exercise>> {
        return exerciseDataSource
            .findByNameContains(search)
            .map { exercises -> exercises.sortedBy { it.name } }

    }
}