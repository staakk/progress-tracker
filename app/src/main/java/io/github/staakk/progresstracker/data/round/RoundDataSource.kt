package io.github.staakk.progresstracker.data.round

import io.github.staakk.progresstracker.data.CreationError
import io.github.staakk.progresstracker.data.DeletionError
import io.github.staakk.progresstracker.data.UpdateError
import io.github.staakk.progresstracker.util.functional.Either
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface RoundDataSource {

    fun create(round: Round): Round

    fun update(round: Round): Round

    fun delete(round: Round): Round

    fun getById(id: String): Round?

    fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round>

    fun getDaysWithRound(start: LocalDateTime, end: LocalDateTime): List<LocalDate>

    fun createSet(set: Set, roundId: String): Either<CreationError, Set>

    fun updateSet(set: Set, roundId: String): Either<UpdateError, Set>

    fun deleteSet(set: Set, roundId: String): Either<DeletionError, Set>
}