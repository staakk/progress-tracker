package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import javax.inject.Inject

class GetExercises @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) {

    operator fun invoke(): List<Exercise> {
        return exerciseDataSource.getAll()
    }

}