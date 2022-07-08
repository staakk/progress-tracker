package io.github.staakk.progresstracker.data.exercise

import io.github.staakk.progresstracker.common.functional.Either
import kotlinx.coroutines.flow.Flow

interface ExerciseDataSource {

    suspend fun create(exercise: Exercise): Either<Error.IdAlreadyExists, Exercise>

    suspend fun update(exercise: Exercise): Either<Error.ExerciseNotFound, Exercise>

    suspend fun delete(exercise: Exercise): Either<Error.ExerciseNotFound, Exercise>

    suspend fun getById(id: String): Either<Error.ExerciseNotFound, Exercise>

    suspend fun findByNameContains(name: String): Flow<List<Exercise>>

    suspend fun findByName(name: String): List<Exercise>

    suspend fun getAll(): List<Exercise>

    interface Error {
        object IdAlreadyExists: Error
        object ExerciseNotFound: Error
    }
}