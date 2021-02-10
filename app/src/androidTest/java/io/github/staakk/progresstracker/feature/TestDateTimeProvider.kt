package io.github.staakk.progresstracker.feature

import io.github.staakk.progresstracker.util.datetime.DateTimeProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.YearMonth

object TestDateTimeProvider : DateTimeProvider {
    override fun currentDateTime(): LocalDateTime =
        LocalDateTime.of(2020, 1, 15, 10, 0)

    override fun currentDate(): LocalDate = LocalDate.from(currentDateTime())

    override fun currentYearMonth(): YearMonth = YearMonth.from(currentDateTime())
}