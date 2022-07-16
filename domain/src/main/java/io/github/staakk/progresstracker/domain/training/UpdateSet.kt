package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class UpdateSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (RoundSet) -> Either<UpdateSet.Error, RoundSet> {

    override suspend fun invoke(set: RoundSet): Either<Error, RoundSet> {
        return trainingDataSource.saveSet(set)
            .mapLeft { Error.UpdateSetError }
    }

    sealed class Error {
        object UpdateSetError: Error()
    }
}