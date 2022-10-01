package io.github.staakk.progresstracker.domain.exercise

import arrow.core.Option
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.Id
import javax.inject.Inject

class GetExerciseById @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource,
) {
    suspend operator fun invoke(id: Id): Option<Exercise> {
        return exerciseDataSource.getById(id)
    }
}