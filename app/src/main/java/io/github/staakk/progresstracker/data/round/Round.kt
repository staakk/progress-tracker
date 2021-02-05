package io.github.staakk.progresstracker.data.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import org.threeten.bp.LocalDateTime
import java.util.*

data class Round(
    val id: String = UUID.randomUUID().toString(),

    /**
     * Exercise executed in this round.
     */
    val exercise: Exercise,

    val createdAt: LocalDateTime,

    /**
     * Sets in this round.
     */
    val roundSets: List<RoundSet> = emptyList(),
) {

    fun withPositionSortedSets() = copy(
        roundSets = roundSets.sortedBy { it.position }
    )
}