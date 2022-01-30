package io.github.staakk.progresstracker.data.round

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.staakk.progresstracker.data.exercise.Exercise
import java.time.ZoneOffset
import java.util.*

@Entity(
    tableName = "Round",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class RoomRound(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "exercise_id") val exerciseId: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
) {

    companion object {
        fun Round.toRoomRound() = RoomRound(
            id = this.id,
            exerciseId = this.exercise.id,
            createdAt = this.createdAt.toEpochSecond(ZoneOffset.UTC)
        )
    }
}