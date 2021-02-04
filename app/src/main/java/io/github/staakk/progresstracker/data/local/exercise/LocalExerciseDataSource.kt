package io.github.staakk.progresstracker.data.local.exercise

import android.database.sqlite.SQLiteConstraintException
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.ExerciseNotFound
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.IdAlreadyExists
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import timber.log.Timber
import javax.inject.Inject

class LocalExerciseDataSource @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseDataSource {

    override fun create(exercise: Exercise): Either<IdAlreadyExists, Exercise> {
        return try {
            exerciseDao.create(exercise)
            exercise.right()
        } catch (e: SQLiteConstraintException) {
            Timber.e(e, "Cannot create exercise. Id ${exercise.id} already exists")
            IdAlreadyExists.left()
        }
    }

    override fun update(exercise: Exercise): Either<ExerciseNotFound, Exercise> {
        return if (exerciseDao.update(exercise) == 1) {
            exercise.right()
        } else {
            ExerciseNotFound.left()
        }
    }

    override fun delete(exercise: Exercise): Either<ExerciseNotFound, Exercise> {
        return if (exerciseDao.delete(exercise) == 1) {
            return exercise.right()
        } else {
            ExerciseNotFound.left()
        }
    }

    override fun getById(id: String): Either<ExerciseNotFound, Exercise> {
        return exerciseDao.getById(id)
            ?.right()
            ?: ExerciseNotFound.left()
    }

    override fun findByName(name: String): List<Exercise> {
        return exerciseDao.findByName("%$name%")
    }

    override fun getAll(): List<Exercise> {
        return exerciseDao.getAll()
    }
}