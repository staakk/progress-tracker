package io.github.staakk.progresstracker.data.round

import java.util.*

/**
 * Represents one set of some exercise.
 */
data class Set(
    /**
     * Unique identifier of this set. If `null` then it wasn't persisted yet.
     */
    val id: String = UUID.randomUUID().toString(),

    val position: Int,

    val reps: Int,

    /**
     * Weight in grams.
     */
    val weight: Int,
)