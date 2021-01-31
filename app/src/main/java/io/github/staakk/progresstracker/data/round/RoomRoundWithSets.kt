package io.github.staakk.progresstracker.data.round

import androidx.room.Embedded
import androidx.room.Relation
import io.github.staakk.progresstracker.data.exercise.Exercise
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

data class RoomRoundWithSets(
    @Embedded val round: RoomRound,

    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "id"
    )
    val exercise: Exercise,

    @Relation(
        parentColumn = "id",
        entityColumn = "round_id"
    )
    val sets: List<RoomSet>
) {

    fun toRound() = Round(
        id = round.id,
        exercise = exercise,
        createdAt = LocalDateTime.ofEpochSecond(round.createdAt, 0, ZoneOffset.UTC)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        sets = sets.map { it.toSet() },
    )
}