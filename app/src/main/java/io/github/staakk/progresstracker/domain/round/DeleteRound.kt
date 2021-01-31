package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.*
import javax.inject.Inject

class DeleteRound @Inject constructor(
    private val roundDataSource: RoundDataSource,
    setDataSource: SetDataSource
) {

    private val removeSetUseCase = DeleteSet(setDataSource)

    operator fun invoke(round: Round): Round? {
        for (set in round.sets) {
            removeSetUseCase(round, set)
        }

        return roundDataSource.delete(round)
    }
}