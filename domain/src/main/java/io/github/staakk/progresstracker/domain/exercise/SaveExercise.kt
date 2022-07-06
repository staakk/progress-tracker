package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.common.functional.Right
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import javax.inject.Inject

class SaveExercise @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) : suspend (Exercise) -> Either<SaveExercise.Error, Exercise> {

    override suspend operator fun invoke(exercise: Exercise): Either<Error, Exercise> {
        return when {
            exercise.nameExists() -> Error.NameAlreadyExists.left()
            exercise.exists() -> exerciseDataSource
                .update(exercise)
                .mapLeft { Error.ExerciseNotFound }
            else -> exerciseDataSource
                .create(exercise)
                .mapLeft { Error.ExerciseAlreadyExists }
        }
    }

    private suspend fun Exercise.exists(): Boolean {
        return exerciseDataSource.getById(id) is Right
    }

    private suspend fun Exercise.nameExists(): Boolean {
        return exerciseDataSource.findByName(name).isNotEmpty()
    }

    sealed class Error {
        object NameAlreadyExists: Error()
        object ExerciseAlreadyExists: Error()
        object ExerciseNotFound: Error()
    }
}