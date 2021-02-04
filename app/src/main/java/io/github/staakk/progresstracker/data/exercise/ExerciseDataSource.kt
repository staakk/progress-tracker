package io.github.staakk.progresstracker.data.exercise

import io.github.staakk.progresstracker.util.functional.Either

interface ExerciseDataSource {

    fun create(exercise: Exercise): Either<Error.IdAlreadyExists, Exercise>

    fun update(exercise: Exercise): Either<Error.ExerciseNotFound, Exercise>

    fun delete(exercise: Exercise): Either<Error.ExerciseNotFound, Exercise>

    fun getById(id: String): Either<Error.ExerciseNotFound, Exercise>

    fun findByName(name: String): List<Exercise>

    fun getAll(): List<Exercise>

    interface Error {
        object IdAlreadyExists: Error
        object ExerciseNotFound: Error
    }
}