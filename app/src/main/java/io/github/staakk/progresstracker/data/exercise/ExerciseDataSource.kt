package io.github.staakk.progresstracker.data.exercise

import io.github.staakk.progresstracker.data.CreationError
import io.github.staakk.progresstracker.data.DeletionError
import io.github.staakk.progresstracker.data.QueryError
import io.github.staakk.progresstracker.data.UpdateError
import io.github.staakk.progresstracker.util.functional.Either

interface ExerciseDataSource {

    fun create(exercise: Exercise): Either<CreationError, Exercise>

    fun update(exercise: Exercise): Either<UpdateError, Exercise>

    fun delete(exercise: Exercise): Either<DeletionError, Exercise>

    fun getById(id: String): Either<QueryError, Exercise>

    fun findByName(name: String): List<Exercise>

    fun getAll(): List<Exercise>
}