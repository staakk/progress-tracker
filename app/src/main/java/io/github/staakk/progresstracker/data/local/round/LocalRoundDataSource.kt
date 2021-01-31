package io.github.staakk.progresstracker.data.local.round

import io.github.staakk.progresstracker.data.round.RoomRound.Companion.toRoomRound
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

class LocalRoundDataSource @Inject constructor(
    private val roundDao: RoundDao
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
}