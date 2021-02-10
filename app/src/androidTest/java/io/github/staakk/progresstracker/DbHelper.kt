package io.github.staakk.progresstracker

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet

fun AppDatabase.initDatabase(
    exercises: List<Exercise> = emptyList(),
    rounds: List<RoomRound> = emptyList(),
    sets: List<RoomSet> = emptyList(),
) {
    clearDatabase()
    val exerciseDao = exerciseDao()
    exercises.forEach(exerciseDao::create)
    val roundDao = roundDao()
    rounds.forEach(roundDao::create)
    val setDao = setDao()
    sets.forEach(setDao::create)
}

fun AppDatabase.clearDatabase() {
    roundDao().apply {
        getAll().forEach {
            delete(it.round)
        }
    }
    exerciseDao().apply {
        getAll().forEach {
            delete(it)
        }
    }
}
