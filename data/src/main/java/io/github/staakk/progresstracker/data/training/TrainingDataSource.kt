package io.github.staakk.progresstracker.data.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.Id
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TrainingDataSource {

    suspend fun saveTraining(training: Training): Either<Error.RoundNotFound, Training>

    suspend fun deleteTraining(training: Training): Either<Error.RoundNotFound, Training>

    fun observeTraining(id: Id): Flow<Training>

    fun queryTrainingByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Training>>

    fun observeRound(id: Id): Flow<Round>

    suspend fun saveRound(round: Round): Either<Error.RoundNotFound, Round>

    suspend fun deleteRound(round: Round): Either<Error.RoundNotFound, Round>

    suspend fun getSetById(setId: Id): Either<Error.RoundNotFound, RoundSet>

    suspend fun saveSet(roundSet: RoundSet): Either<Error.CreateSetError, RoundSet>

    suspend fun deleteSet(roundSet: RoundSet): Either<Error.DeleteSetError, RoundSet>

    interface Error {
        object RoundAlreadyExists : Error

        object RoundNotFound : Error

        sealed class CreateSetError : Error {
            object SetAlreadyExist : CreateSetError()
            object RoundNotFound : CreateSetError()
        }

        sealed class UpdateSetError : Error {
            object SetNotFound : UpdateSetError()
            object RoundNotFound : UpdateSetError()
        }

        sealed class DeleteSetError : Error {
            object SetNotFound : DeleteSetError()
        }
    }
}