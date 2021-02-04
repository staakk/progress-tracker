package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import timber.log.Timber
import javax.inject.Inject

class CreateExercise @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource,
) {

    operator fun invoke(exercise: Exercise): Either<Error, Exercise> {
        val resultFound = exerciseDataSource.findByName(exercise.name)
            .isNotEmpty()

        if (resultFound) {
            Timber.e("Exercise with name ${exercise.name} already exists.")
            return Error.ExerciseWithNameAlreadyExists.left()
        }

        return exerciseDataSource.create(exercise)
            .mapLeft { Error.ExerciseWithIdAlreadyExists }
    }

    sealed class Error {
        object ExerciseWithNameAlreadyExists : Error()
        object ExerciseWithIdAlreadyExists : Error()
    }
}