package io.github.staakk.progresstracker

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet

object DbHelper {

    private val EXERCISES = listOf(
        Exercise(name = "Dead lift"),
        Exercise(name = "Low bar squat"),
        Exercise(name = "Bench press")
    )

    fun initDatabase(
        db: AppDatabase,
        exercises: List<Exercise> = EXERCISES,
        rounds: List<RoomRound> = emptyList(),
        sets: List<RoomSet> = emptyList()
    ) {
        clearDatabase(db)
        db.exerciseDao().apply {
            exercises.forEach(this::create)
        }
        db.roundDao().apply {
            rounds.forEach(this::create)
        }
        db.setDao().apply {
            sets.forEach(this::create)

        }
    }

    fun clearDatabase(db: AppDatabase) {
        db.roundDao().apply {
            getAll().forEach {
                delete(it.round)
            }
        }
        db.exerciseDao().apply {
            getAll().forEach {
                delete(it)
            }
        }
    }
}