package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.Id
import javax.inject.Inject

class GetExerciseById @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource,
) {
    suspend operator fun invoke(id: Id): Either<Error, Exercise> {
        return exerciseDataSource.getById(id)
            .mapLeft { Error.ExerciseNotFound }
    }

    sealed class Error {
        object ExerciseNotFound : Error()
    }
}