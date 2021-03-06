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

    suspend operator fun invoke(name: String): Either<Error, Exercise> {
        val resultFound = exerciseDataSource.findByName(name)
            .isNotEmpty()

        if (resultFound) {
            Timber.e("Exercise with name $name already exists.")
            return Error.ExerciseWithNameAlreadyExists.left()
        }

        return exerciseDataSource.create(Exercise(name = name))
            .mapLeft { Error.ExerciseWithIdAlreadyExists }
    }

    sealed class Error {
        object ExerciseWithNameAlreadyExists : Error()
        object ExerciseWithIdAlreadyExists : Error()
    }
}