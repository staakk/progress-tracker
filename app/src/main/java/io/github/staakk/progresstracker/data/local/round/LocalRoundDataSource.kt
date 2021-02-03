package io.github.staakk.progresstracker.data.local.round

import android.database.sqlite.SQLiteConstraintException
import io.github.staakk.progresstracker.data.CreationError
import io.github.staakk.progresstracker.data.DeletionError
import io.github.staakk.progresstracker.data.UpdateError
import io.github.staakk.progresstracker.data.round.RoomRound.Companion.toRoomRound
import io.github.staakk.progresstracker.data.round.RoomSet.Companion.toRoomSet
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

class LocalRoundDataSource @Inject constructor(
    private val roundDao: RoundDao,
    private val setDao: SetDao
) : RoundDataSource {
    override fun create(round: Round): Round {
        roundDao.create(round.toRoomRound())
        return round
    }

    override fun update(round: Round): Round {
        roundDao.update(round.toRoomRound())
        return round
    }

    override fun delete(round: Round): Round {
        roundDao.delete(round.toRoomRound())
        return round
    }

    override fun getById(id: String): Round? {
        return roundDao.getById(id)?.toRound()
    }

    override fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round> {
        return roundDao.getByDate(
            start.toEpochSecond(ZoneOffset.UTC),
            end.toEpochSecond(ZoneOffset.UTC)
        ).map { it.toRound() }
    }

    override fun getDaysWithRound(start: LocalDateTime, end: LocalDateTime): List<LocalDate> {
        return roundDao.getDaysWithRound(
            start.toEpochSecond(ZoneOffset.UTC),
            end.toEpochSecond(ZoneOffset.UTC)
        ).map {
            LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }

    override fun createSet(set: Set, roundId: String): Either<CreationError, Set> {
        return try {
            setDao.create(set.toRoomSet(roundId))
            set.right()
        } catch (e: SQLiteConstraintException) {
            CreationError.IdAlreadyExists.left()
        }
    }

    override fun updateSet(set: Set, roundId: String): Either<UpdateError, Set> {
        return if (setDao.update(set.toRoomSet(roundId)) == 1) {
            set.right()
        } else {
            UpdateError.ResourceDoesNotExist.left()
        }
    }

    override fun deleteSet(set: Set, roundId: String): Either<DeletionError, Set> {
        return if (setDao.delete(set.toRoomSet(roundId)) == 1) {
            set.right()
        } else {
            DeletionError.CannotDeleteResource.left()
        }
    }}