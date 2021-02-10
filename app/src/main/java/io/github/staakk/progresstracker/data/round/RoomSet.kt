package io.github.staakk.progresstracker.data.round

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "Set",
    foreignKeys = [
        ForeignKey(
            entity = RoomRound::class,
            parentColumns = ["id"],
            childColumns = ["round_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class RoomSet(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val position: Int,

    val reps: Int,

    /**
     * Weight in grams.
     */
    val weight: Int,

    @ColumnInfo(name = "round_id") val roundId: String,
) {
    fun toSet() = RoundSet(
        id = id,
        position = position,
        reps = reps,
        weight = weight,
    )

    companion object {
        fun RoundSet.toRoomSet(roundId: String) = RoomSet(
            id = id,
            position = position,
            reps = reps,
            weight = weight,
            roundId = roundId
        )
    }
}