package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import timber.log.Timber
import javax.inject.Inject

class CreateExercise @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) {

    operator fun invoke(exercise: Exercise) : Either<Error, Exercise> {
        val resultFound = exerciseDataSource.findByName(exercise.name)
            .isNotEmpty()

        if (resultFound) {
            Timber.e("Exercise with name ${exercise.name} already exists.")
            return Error.ExerciseWithNameAlreadyExists(exercise.name).left()
        }

        return exerciseDataSource.create(exercise).right()
    }

    sealed class Error {
        data class ExerciseWithNameAlreadyExists(val name: String) : Error()
    }
}