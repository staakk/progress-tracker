package io.github.staakk.progresstracker.data.round

import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface RoundDataSource {

    suspend fun create(round: Round): Either<Error.RoundAlreadyExists, Round>

    suspend fun update(round: Round): Either<Error.RoundNotFound, Round>

    suspend fun delete(round: Round): Either<Error.RoundNotFound, Round>

    suspend fun getById(id: String): Either<Error.RoundNotFound, Round>

    suspend fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round>

    suspend fun getDaysWithRound(start: LocalDateTime, end: LocalDateTime): List<LocalDate>

    suspend fun createSet(roundSet: RoundSet, roundId: String): Either<Error.CreateSetError, RoundSet>

    suspend fun updateSet(roundSet: RoundSet, roundId: String): Either<Error.UpdateSetError, RoundSet>

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