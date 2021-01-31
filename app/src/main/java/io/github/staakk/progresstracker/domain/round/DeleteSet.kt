package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import io.github.staakk.progresstracker.data.round.Set
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val setDataSource: SetDataSource
) {

    operator fun invoke(round: Round, set: Set): Round {
        setDataSource.delete(set, round.id)
        return round.copy(sets = round.sets - set)
    }
}