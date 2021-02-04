package io.github.staakk.progresstracker.data.round

import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface RoundDataSource {

    fun create(round: Round): Either<Error.RoundAlreadyExists, Round>

    fun update(round: Round): Either<Error.RoundNotFound, Round>

    fun delete(round: Round): Either<Error.RoundNotFound, Round>

    fun getById(id: String): Either<Error.RoundNotFound, Round>

    fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round>

    fun getDaysWithRound(start: LocalDateTime, end: LocalDateTime): List<LocalDate>

    fun createSet(set: Set, roundId: String): Either<Error.CreateSetError, Set>

    fun updateSet(set: Set, roundId: String): Either<Error.UpdateSetError, Set>

    fun deleteSet(set: Set): Either<Error.DeleteSetError, Set>

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