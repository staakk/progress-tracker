package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.RoundDataSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class GetRoundsByDateTime @Inject constructor(
    private val roundDataSource: RoundDataSource
) {

    operator fun invoke(from: LocalDateTime, to: LocalDateTime) =
        roundDataSource.getByDate(from, to).map { it.withPositionSortedSets() }

    operator fun invoke(day: LocalDate) =
        invoke(day.atStartOfDay(), day.atStartOfDay().plusHours(24))
}