package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import javax.inject.Inject

class CreateRound @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : suspend (Training) -> Either<CreateRound.Error, Pair<Training, Round>> {

    override suspend fun invoke(training: Training): Either<Error, Pair<Training, Round>> {
        val newRound = Round(
            ordinal = training.rounds.nextOrdinal(),
            exercise = Exercise.EmptyExercise,
        )
        val updatedTraining = training.copy(rounds = training.rounds + newRound)
        return trainingDataSource.saveTraining(updatedTraining)
            .mapLeft { Error.CreateRoundError }
            .map { saved -> saved to saved.rounds.first { it.ordinal == newRound.ordinal } }
    }

    sealed class Error {
        object CreateRoundError : Error()
    }

    private fun List<Round>.nextOrdinal() = maxOfOrNull { it.ordinal }
        ?.let(Int::inc)
        ?: 0
}