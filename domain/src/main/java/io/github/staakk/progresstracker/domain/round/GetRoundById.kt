package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import javax.inject.Inject

class GetRoundById @Inject constructor(
    private val roundDataSource: RoundDataSource,
) {

    suspend operator fun invoke(id: String): Either<Error, Round> {
        return roundDataSource.getById(id)
            .fold(
                { Error.RoundNotFound.left() },
                { it.withPositionSortedSets().right() }
            )
    }

    sealed class Error {
        object RoundNotFound : Error()
    }
}