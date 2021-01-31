package io.github.staakk.progresstracker.data.round

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @PrimaryKey val id: String,

    val position: Int,

    val reps: Int,

    /**
     * Weight in grams.
     */
    val weight: Int,

    @ColumnInfo(name = "round_id") val roundId: String,
) {
    fun toSet() = Set(
        id = id,
        position = position,
        reps = reps,
        weight = weight,
    )

    companion object {
        fun Set.toRoomSet(roundId: String) = RoomSet(
            id = id,
            position = position,
            reps = reps,
            weight = weight,
            roundId = roundId
        )
    }
}