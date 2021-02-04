package io.github.staakk.progresstracker.util.log

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

fun LocalDateTime.format() {
    format(DateTimeFormatter.ISO_DATE_TIME)
}

fun LocalDate.format() {
    format(DateTimeFormatter.ISO_DATE)
}