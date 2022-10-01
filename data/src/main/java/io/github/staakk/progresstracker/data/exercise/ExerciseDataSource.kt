package io.github.staakk.progresstracker.data.exercise

import arrow.core.Option
import io.github.staakk.progresstracker.data.Id
import kotlinx.coroutines.flow.Flow

interface ExerciseDataSource {

    suspend fun save(exercise: Exercise): Option<Exercise>

    suspend fun delete(exercise: Exercise): Option<Exercise>

    suspend fun getById(id: Id): Option<Exercise>

    suspend fun findByNameContains(name: String): Flow<List<Exercise>>

    suspend fun findByName(name: String): List<Exercise>

    suspend fun getAll(): List<Exercise>
}