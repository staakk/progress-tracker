package io.github.staakk.progresstracker.common.log

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.format() {
    format(DateTimeFormatter.ISO_DATE_TIME)
}

fun LocalDate.format() {
    format(DateTimeFormatter.ISO_DATE)
}