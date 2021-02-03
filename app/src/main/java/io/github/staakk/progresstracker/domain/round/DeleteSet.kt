package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.data.round.Set
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val roundDataSource: RoundDataSource
) {

    operator fun invoke(round: Round, set: Set): Round {
        roundDataSource.deleteSet(set, round.id)
        return round.copy(sets = round.sets - set)
    }
}