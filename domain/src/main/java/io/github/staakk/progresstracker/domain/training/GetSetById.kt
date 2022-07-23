package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class GetSetById @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (Id) -> Either<GetSetById.Error, RoundSet> {

    override suspend fun invoke(setId: Id): Either<Error, RoundSet> {
        return trainingDataSource.getSetById(setId)
            .mapLeft { Error.SetNotFound }
    }

    sealed class Error {
        object SetNotFound : Error()
    }
}