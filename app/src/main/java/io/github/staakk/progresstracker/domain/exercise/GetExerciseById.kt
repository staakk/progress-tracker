package io.github.staakk.progresstracker.domain.exercise

import io.github.staakk.progresstracker.data.QueryError
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import javax.inject.Inject

class GetExerciseById @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) {
    operator fun invoke(id: String): Either<Error, Exercise> {
        return exerciseDataSource.getById(id)
            .mapLeft {
                when (it) {
                    QueryError.ResourceNotFound -> Error.IdNotFound
                }
            }
    }

    sealed class Error {
        object IdNotFound: Error()
    }
}