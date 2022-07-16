package io.github.staakk.progresstracker.data.training

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise

data class Round(
    val id: Id = Id.Empty,

    val ordinal: Int,

    /**
     * Exercise executed in this round.
     */
    val exercise: Exercise,

    /**
     * Sets in this round.
     */
    val roundSets: List<RoundSet> = emptyList(),
) {

    fun withPositionSortedSets() = copy(
        roundSets = roundSets.sortedBy { it.ordinal }
    )
}