package io.github.staakk.progresstracker.domain.round

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundDataSource
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CreateRound @Inject constructor(
    private val roundDataSource: RoundDataSource
) {

    operator fun invoke(createdAt: LocalDate, exercise: Exercise): Round  {
        return roundDataSource.create(Round(exercise = exercise, createdAt = createdAt.atStartOfDay()))
    }
}