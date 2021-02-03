package io.github.staakk.progresstracker.data.local.round

import androidx.room.*
import io.github.staakk.progresstracker.data.round.RoomRoundWithSets
import io.github.staakk.progresstracker.data.round.RoomSet

@Dao
interface SetDao {

    @Insert
    fun create(set: RoomSet)

    @Update
    fun update(set: RoomSet) : Int

    @Delete
    fun delete(set: RoomSet) : Int
}