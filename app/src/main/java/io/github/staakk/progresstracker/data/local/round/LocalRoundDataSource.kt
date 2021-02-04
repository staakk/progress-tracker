package io.github.staakk.progresstracker.data.local.round

import android.database.sqlite.SQLiteConstraintException
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.round.RoomRound.Companion.toRoomRound
import io.github.staakk.progresstracker.data.round.RoomSet.Companion.toRoomSet
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.RoundDataSource.Error.*
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import timber.log.Timber
import javax.inject.Inject

class LocalRoundDataSource @Inject constructor(
    private val roundDao: RoundDao,
    private val setDao: SetDao,
) : RoundDataSource {

    override fun create(round: Round): Either<RoundAlreadyExists, Round> {
        return try {
            roundDao.create(round.toRoomRound())
            round.right()
        } catch (e: SQLiteConstraintException) {
            Timber.e(e, "Cannot create exercise. Id ${round.id} already exists")
            RoundAlreadyExists.left()
        }
    }

    override fun update(round: Round): Either<RoundNotFound, Round> {
        return if (roundDao.update(round.toRoomRound()) == 1) {
            round.right()
        } else {
            Timber.e("Cannot update round ${round.id}")
            RoundNotFound.left()
        }
    }

    override fun delete(round: Round): Either<RoundNotFound, Round> {
        return if (roundDao.delete(round.toRoomRound()) == 1) {
            round.right()
        } else {
            Timber.e("Cannot delete round ${round.id}")
            RoundNotFound.left()
        }
    }

    override fun getById(id: String): Either<RoundNotFound, Round> {
        return roundDao.getById(id)
            ?.toRound()
            ?.right()
            ?: RoundNotFound.left()
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

    override fun createSet(set: Set, roundId: String): Either<CreateSetError, Set> {
        return try {
            setDao.create(set.toRoomSet(roundId))
            set.right()
        } catch (e: SQLiteConstraintException) {
            getById(roundId)
                .fold(
                    { CreateSetError.RoundNotFound },
                    { CreateSetError.SetAlreadyExist })
                .left()
        }
    }

    override fun updateSet(set: Set, roundId: String): Either<UpdateSetError, Set> {
        return try {
            if (setDao.update(set.toRoomSet(roundId)) == 1) {
                set.right()
            } else {
                UpdateSetError.SetNotFound.left()
            }
        } catch (e: SQLiteConstraintException) {
            UpdateSetError.RoundNotFound.left()
        }
    }

    override fun deleteSet(set: Set): Either<DeleteSetError, Set> {
        return if (setDao.delete(set.toRoomSet("not required")) == 1) {
            set.right()
        } else {
            DeleteSetError.SetNotFound.left()
        }
    }
}