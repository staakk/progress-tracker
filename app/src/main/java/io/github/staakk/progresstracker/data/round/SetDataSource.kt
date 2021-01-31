package io.github.staakk.progresstracker.data.round

import io.github.staakk.progresstracker.data.round.Set

interface SetDataSource {

    fun create(set: Set, roundId: String): Set

    fun update(set: Set, roundId: String): Set

    fun delete(set: Set, roundId: String): Set
}