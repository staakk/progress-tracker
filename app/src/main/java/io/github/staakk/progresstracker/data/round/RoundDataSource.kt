package io.github.staakk.progresstracker.data.round

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface RoundDataSource {

    fun create(round: Round): Round

    fun update(round: Round): Round

    fun delete(round: Round): Round

    fun getById(id: String): Round?

    fun getByDate(start: LocalDateTime, end: LocalDateTime): List<Round>

    fun getDaysWithRound(start: LocalDateTime, end: LocalDateTime): List<LocalDate>
}