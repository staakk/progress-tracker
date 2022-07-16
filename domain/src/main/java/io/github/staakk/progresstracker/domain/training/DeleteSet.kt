package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class DeleteSet @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (RoundSet) -> Either<DeleteSet.Error, RoundSet> {

    override suspend fun invoke(roundSet: RoundSet): Either<Error, RoundSet> =
        trainingDataSource.deleteSet(roundSet)
            .mapLeft { Error.DeleteSetError }


    sealed class Error {
        object DeleteSetError: Error()
    }
}