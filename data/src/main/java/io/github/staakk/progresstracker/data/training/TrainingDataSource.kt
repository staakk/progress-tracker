package io.github.staakk.progresstracker.data.training

import arrow.core.Option
import io.github.staakk.progresstracker.data.Id
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TrainingDataSource {

    suspend fun saveTraining(training: Training): Option<Training>

    suspend fun deleteTraining(training: Training): Option<Training>

    fun observeTraining(id: Id): Flow<Training>

    fun queryTrainingByDate(
        exerciseQuery: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Training>>

    fun observeRound(id: Id): Flow<Round>

    suspend fun saveRound(round: Round): Option<Round>

    suspend fun deleteRound(round: Round): Option<Round>

    suspend fun getSetById(setId: Id): Option<RoundSet>

    suspend fun saveSet(roundSet: RoundSet): Option<RoundSet>

    suspend fun deleteSet(roundSet: RoundSet): Option<RoundSet>
}