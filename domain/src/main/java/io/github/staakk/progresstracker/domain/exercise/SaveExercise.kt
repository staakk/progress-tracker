package io.github.staakk.progresstracker.domain.exercise

import arrow.core.Either
import arrow.core.left
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import javax.inject.Inject

class SaveExercise @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) : suspend (Exercise) -> Either<SaveExercise.Error, Exercise> {

    override suspend operator fun invoke(exercise: Exercise): Either<Error, Exercise> {
        return when {
            exercise.nameExists() -> Error.NameAlreadyExists.left()
            else -> exerciseDataSource
                .save(exercise)
                .toEither { Error.ExerciseNotFound }
        }
    }

    private suspend fun Exercise.nameExists(): Boolean {
        return exerciseDataSource.findByName(name).isNotEmpty()
    }

    sealed class Error {
        object NameAlreadyExists: Error()
        object ExerciseNotFound: Error()
    }
}