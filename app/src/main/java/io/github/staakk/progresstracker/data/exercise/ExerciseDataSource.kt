package io.github.staakk.progresstracker.data.exercise

interface ExerciseDataSource {

    fun create(exercise: Exercise): Exercise

    fun update(exercise: Exercise): Exercise

    fun delete(exercise: Exercise): Exercise

    fun getById(id: String): Exercise?

    fun findByName(name: String): List<Exercise>

    fun getAll(): List<Exercise>
}