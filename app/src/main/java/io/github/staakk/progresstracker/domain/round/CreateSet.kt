package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.data.round.SetDataSource
import javax.inject.Inject

class CreateSet @Inject constructor(
    private val setDataSource: SetDataSource
) {

    operator fun invoke(round: Round, set: Set): Round {
        setDataSource.create(set, round.id)
        return round.copy(sets = round.sets + set)
    }
}