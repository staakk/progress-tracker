package io.github.staakk.progresstracker.feature

import io.github.staakk.common.ui.compose.datetime.DateTimeProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

object TestDateTimeProvider : DateTimeProvider {
    override fun currentDateTime(): LocalDateTime =
        LocalDateTime.of(2020, 1, 15, 10, 0)

    override fun currentDate(): LocalDate = LocalDate.from(currentDateTime())

    override fun currentYearMonth(): YearMonth = YearMonth.from(currentDateTime())
}