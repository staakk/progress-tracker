package io.github.staakk.progresstracker.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

class PopulateDatabaseCallback : RoomDatabase.Callback() {

    // TODO put this in one sql file. Probably won't use init with db since it's not flexible enough for development/testing.
    override fun onCreate(db: SupportSQLiteDatabase) {
        GlobalScope.launch {
            val values = INITIAL_EXERCISES.joinToString(separator = ", ") { "('${it.id}', '${it.name}')" }
            db.execSQL("INSERT INTO Exercise (id, name) VALUES $values;")

            val valuesRounds = DEBUG_ROUNDS.joinToString(separator = ", ") { "('${it.id}', '${it.exerciseId}', ${it.createdAt})" }
            db.execSQL("INSERT INTO Round (id, exercise_id, created_at) VALUES $valuesRounds;")

            val valuesSets = DEBUG_SETS.joinToString(separator = ", ") { "('${it.id}', ${it.position}, ${it.reps}, ${it.weight}, '${it.roundId}')" }
            db.execSQL("INSERT INTO `Set` (id, position, reps, weight, round_id) VALUES $valuesSets;")
        }
    }

    companion object {
        private val INITIAL_EXERCISES = listOf(
            Exercise(name = "Dead lift"),
            Exercise(name = "Low bar squat"),
            Exercise(name = "High bar squat"),
            Exercise(name = "Front bar squat"),
            Exercise(name = "Barbell row"),
            Exercise(name = "Dumbbell row"),
            Exercise(name = "Pull-ups"),
            Exercise(name = "Push-ups"),
            Exercise(name = "Overhead press"),
            Exercise(name = "Bicep curls"),
            Exercise(name = "Bench press"),
        )
        private val DEBUG_ROUNDS = listOf(
            RoomRound(UUID.randomUUID().toString(), INITIAL_EXERCISES[0].id, LocalDateTime.of(2021, 1, 22, 10, 10).toEpochSecond(
                ZoneOffset.UTC)),
            RoomRound(UUID.randomUUID().toString(), INITIAL_EXERCISES[1].id, LocalDateTime.of(2020, 12, 28, 10, 10).toEpochSecond(
                ZoneOffset.UTC)),
        )
        private val DEBUG_SETS = listOf(
            RoomSet(UUID.randomUUID().toString(), 1, 10, 80, DEBUG_ROUNDS[0].id),
            RoomSet(UUID.randomUUID().toString(), 2, 10, 90, DEBUG_ROUNDS[0].id),
            RoomSet(UUID.randomUUID().toString(), 3, 10, 100, DEBUG_ROUNDS[0].id),
            RoomSet(UUID.randomUUID().toString(), 1, 8, 80, DEBUG_ROUNDS[1].id),
            RoomSet(UUID.randomUUID().toString(), 2, 5, 90, DEBUG_ROUNDS[1].id),
            RoomSet(UUID.randomUUID().toString(), 3, 6, 100, DEBUG_ROUNDS[1].id),
        )
    }
}