package io.github.staakk.progresstracker.data.local.round

import androidx.room.*
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomRoundWithSets

@Dao
interface RoundDao {

    @Insert
    fun create(round: RoomRound)

    @Update
    fun update(round: RoomRound): Int

    @Delete
    fun delete(round: RoomRound): Int

    @Transaction
    @Query("SELECT * FROM round WHERE id == :id")
    fun getById(id: String): RoomRoundWithSets?

    @Transaction
    @Query("SELECT * FROM round")
    fun getAll(): List<RoomRoundWithSets>

    @Transaction
    @Query("SELECT * FROM round WHERE created_at BETWEEN :from AND :to")
    fun getByDate(from: Long, to: Long): List<RoomRoundWithSets>

    @Query("SELECT created_at FROM round WHERE created_at BETWEEN :from AND :to")
    fun getDaysWithRound(from: Long, to: Long): List<Long>

}