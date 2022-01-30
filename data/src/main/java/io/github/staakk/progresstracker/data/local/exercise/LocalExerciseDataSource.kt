package io.github.staakk.progresstracker.data.local.exercise

import android.database.sqlite.SQLiteConstraintException
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.ExerciseNotFound
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.IdAlreadyExists
import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalExerciseDataSource(
    private val exerciseDao: ExerciseDao,
    private val dispatcher: CoroutineDispatcher,
) : ExerciseDataSource {

    override suspend fun create(exercise: Exercise): Either<IdAlreadyExists, Exercise> =
        withContext(dispatcher) {
            try {
                exerciseDao.create(exercise)
                exercise.right()
            } catch (e: SQLiteConstraintException) {
                Timber.e(e, "Cannot create exercise. Id ${exercise.id} already exists")
                IdAlreadyExists.left()
            }
        }

    override suspend fun update(exercise: Exercise): Either<ExerciseNotFound, Exercise> =
        withContext(dispatcher) {
            if (exerciseDao.update(exercise) == 1) {
                exercise.right()
            } else {
                ExerciseNotFound.left()
            }
        }

    override suspend fun delete(exercise: Exercise): Either<ExerciseNotFound, Exercise> =
        withContext(dispatcher) {
            if (exerciseDao.delete(exercise) == 1) {
                exercise.right()
            } else {
                ExerciseNotFound.left()
            }
        }

    override suspend fun getById(id: String): Either<ExerciseNotFound, Exercise> =
        withContext(dispatcher) {
            exerciseDao.getById(id)?.right() ?: ExerciseNotFound.left()
        }

    override suspend fun findByName(name: String): List<Exercise> = withContext(dispatcher) {
        exerciseDao.findByName("%$name%")
    }

    override suspend fun getAll(): List<Exercise> = withContext(dispatcher) {
        exerciseDao.getAll()
    }
}