package io.github.staakk.ui.trainings

import io.github.staakk.progresstracker.data.training.Training

sealed class TrainingsEvent {

    object ScreenOpened : TrainingsEvent()

    object CreateNewTraining : TrainingsEvent()

    data class EditTraining(val training: Training) : TrainingsEvent()

    data class DeleteTraining(val training: Training) : TrainingsEvent()
}