package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import timber.log.Timber
import javax.inject.Inject

class UpdateExercise @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) {

    operator fun invoke(exercise: Exercise, name: String): Either<Error, Exercise> {
        val resultFound = exerciseDataSource.findByName(name)
            .isNotEmpty()

        if (resultFound) {
            Timber.e("Exercise with name $name already exists.")
            return Error.NameAlreadyExists.left()
        }

        return exerciseDataSource.update(exercise.copy(name = name)).right()
    }

    sealed class Error {
        object NameAlreadyExists : Error()
    }
}