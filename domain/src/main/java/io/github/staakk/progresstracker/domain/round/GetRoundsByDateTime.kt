package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.common.log.format
import java.time.LocalDate
import java.time.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class GetRoundsByDateTime @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(from: LocalDateTime, to: LocalDateTime): List<Round> {
        if (to.isBefore(from)) {
            Timber.w("`to` date (${to.format()} before `from` date (${from.format()}")
            return emptyList()
        }
        return roundDataSource.getByDate(from, to)
            .map { it.withPositionSortedSets() }
    }

    suspend operator fun invoke(day: LocalDate) =
        invoke(day.atStartOfDay(), day.atStartOfDay().plusHours(24))
}