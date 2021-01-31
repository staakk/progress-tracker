package io.github.staakk.progresstracker.data.local.exercise

import androidx.room.*
import io.github.staakk.progresstracker.data.exercise.Exercise

@Dao
interface ExerciseDao {

    @Insert
    fun create(exercise: Exercise)

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE id == :id")
    fun getById(id: String): Exercise

    @Query("SELECT * FROM exercise WHERE name LIKE :name")
    fun findByName(name: String): List<Exercise>
}