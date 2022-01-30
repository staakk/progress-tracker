package io.github.staakk.common.ui.compose

import java.time.format.DateTimeFormatter

object Formatters {
    val DAY_MONTH_SHORT_YEAR_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yy")
}
