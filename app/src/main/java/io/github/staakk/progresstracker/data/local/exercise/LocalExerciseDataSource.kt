package io.github.staakk.progresstracker.data.local.exercise

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import javax.inject.Inject

class LocalExerciseDataSource @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseDataSource {

    override fun create(exercise: Exercise): Exercise {
        exerciseDao.create(exercise)
        return exercise
    }

    override fun update(exercise: Exercise): Exercise {
        exerciseDao.update(exercise)
        return exercise
    }

    override fun delete(exercise: Exercise): Exercise {
        exerciseDao.delete(exercise)
        return exercise
    }

    override fun getById(id: String): Exercise {
        return exerciseDao.getById(id)
    }

    override fun findByName(name: String): List<Exercise> {
        return exerciseDao.findByName("%$name%")
    }

    override fun getAll(): List<Exercise> {
        return exerciseDao.getAll()
    }
}