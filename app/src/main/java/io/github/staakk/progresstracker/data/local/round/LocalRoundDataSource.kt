package io.github.staakk.progresstracker.data.local.round

import android.database.sqlite.SQLiteConstraintException
import io.github.staakk.progresstracker.data.round.RoomRound.Companion.toRoomRound
import io.github.staakk.progresstracker.data.round.RoomSet.Companion.toRoomSet
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.*
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import timber.log.Timber

class LocalRoundDataSource(
    private val roundDao: RoundDao,
    private val setDao: SetDao,
    private val dispatcher: CoroutineDispatcher,
) : RoundDataSource {

    override suspend fun create(round: Round): Either<RoundAlreadyExists, Round> =
        withContext(dispatcher) {
            try {
                roundDao.create(round.toRoomRound())
                round.right()
            } catch (e: SQLiteConstraintException) {
                Timber.e(e, "Cannot create exercise. Id ${round.id} already exists")
                RoundAlreadyExists.left()
            }
        }

    override suspend fun update(round: Round): Either<RoundNotFound, Round> =
        withContext(dispatcher) {
            if (roundDao.update(round.toRoomRound()) == 1) {
                round.right()
            } else {
                Timber.e("Cannot update round ${round.id}")
                RoundNotFound.left()
            }
        }

    override suspend fun delete(round: Round): Either<RoundNotFound, Round> =
        withContext(dispatcher) {
            if (roundDao.delete(round.toRoomRound()) == 1) {
                round.right()
            } else {
                Timber.e("Cannot delete round ${round.id}")
                RoundNotFound.left()
            }
        }

    override suspend fun getById(id: String): Either<RoundNotFound, Round> =
        withContext(dispatcher) {
            roundDao.getById(id)
                ?.toRound()
                ?.right()
                ?: RoundNotFound.left()
        }

    override suspend fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round> =
        withContext(dispatcher) {
            roundDao.getByDate(
                start.toEpochSecond(ZoneOffset.UTC),
                end.toEpochSecond(ZoneOffset.UTC)
            ).map { it.toRound() }
        }

    override suspend fun getDaysWithRound(
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<LocalDate> = withContext(dispatcher) {
        roundDao.getDaysWithRound(
            start.toEpochSecond(ZoneOffset.UTC),
            end.toEpochSecond(ZoneOffset.UTC)
        ).map {
            LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }

    override suspend fun createSet(
        roundSet: RoundSet,
        roundId: String,
    ): Either<CreateSetError, RoundSet> = withContext(dispatcher) {
        try {
            setDao.create(roundSet.toRoomSet(roundId))
            roundSet.right()
        } catch (e: SQLiteConstraintException) {
            getById(roundId)
                .fold(
                    { CreateSetError.RoundNotFound },
                    { CreateSetError.SetAlreadyExist })
                .left()
        }
    }

    override suspend fun updateSet(
        roundSet: RoundSet,
        roundId: String,
    ): Either<UpdateSetError, RoundSet> = withContext(dispatcher) {
        try {
            if (setDao.update(roundSet.toRoomSet(roundId)) == 1) {
                roundSet.right()
            } else {
                UpdateSetError.SetNotFound.left()
            }
        } catch (e: SQLiteConstraintException) {
            UpdateSetError.RoundNotFound.left()
        }
    }

    override suspend fun deleteSet(roundSet: RoundSet): Either<DeleteSetError, RoundSet> =
        withContext(dispatcher) {
            if (setDao.delete(roundSet.toRoomSet("not required")) == 1) {
                roundSet.right()
            } else {
                DeleteSetError.SetNotFound.left()
            }
        }
}