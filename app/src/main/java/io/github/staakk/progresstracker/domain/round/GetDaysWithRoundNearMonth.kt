package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.RoundDataSource
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetDaysWithRoundNearMonth @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(date: YearMonth): List<LocalDate> =
        roundDataSource.getDaysWithRound(
            date.atDay(1).minusDays(15).atStartOfDay(),
            date.atEndOfMonth().plusDays(15).atStartOfDay()
        )
}