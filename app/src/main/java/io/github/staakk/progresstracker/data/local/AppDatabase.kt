package io.github.staakk.progresstracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.local.exercise.ExerciseDao
import io.github.staakk.progresstracker.data.local.round.RoundDao
import io.github.staakk.progresstracker.data.local.round.SetDao
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet

@Database(
    entities = [Exercise::class, RoomRound::class, RoomSet::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    abstract fun roundDao(): RoundDao

    abstract fun setDao(): SetDao

    companion object {
        const val DB_NAME = "main"
    }
}