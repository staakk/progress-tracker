package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.Set
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val roundDataSource: RoundDataSource
) {

    operator fun invoke(round: Round, set: Set): Round {
        roundDataSource.createSet(set, round.id)
        return round.copy(sets = round.sets + set)
    }
}