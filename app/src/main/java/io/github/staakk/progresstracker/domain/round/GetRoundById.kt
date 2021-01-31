package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.util.functional.Either
import io.github.staakk.progresstracker.util.functional.left
import io.github.staakk.progresstracker.util.functional.right
import javax.inject.Inject

class GetRoundById @Inject constructor(
    private val roundDataSource: RoundDataSource
) {

    operator fun invoke(id: String): Either<Error, Round> {
        return roundDataSource.getById(id)
            ?.withPositionSortedSets()
            ?.right()
            ?: Error.IdNotFound.left()
    }

    sealed class Error {
        object IdNotFound: Error()
    }
}