package io.github.staakk.progresstracker.data.training

import io.github.staakk.progresstracker.data.Id

/**
 * Represents one set of some exercise.
 */
data class RoundSet(
    /**
     * Unique identifier of this set. If `null` then it wasn't persisted yet.
     */
    val id: Id = Id.Empty,

    val ordinal: Int,

    val reps: Int,

    /**
     * Weight in grams.
     */
    // TODO create value class for weight
    val weight: Int,
)