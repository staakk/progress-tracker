package io.github.staakk.ui.training

import io.github.staakk.progresstracker.data.training.Training

sealed class TrainingState {
    object Loading : TrainingState()

    data class Loaded(val training: Training) : TrainingState()
}