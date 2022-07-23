package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.Training
import java.time.LocalDateTime

object TrainingPreviewData {

    val exercises = listOf(
        Exercise(name = "Exercise name"),
    )

    val set = RoundSet(
        ordinal = 1,
        reps = 2,
        weight = 50
    )

    val round = Round(
        ordinal = 1,
        exercise = exercises[0],
        roundSets = listOf(
            set,
            set.copy(),
        )
    )

    val training = Training(
        date = LocalDateTime.now(),
        rounds = listOf(
            round,
            round.copy()
        )
    )
}