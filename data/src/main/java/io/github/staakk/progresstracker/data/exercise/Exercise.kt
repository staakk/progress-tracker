package io.github.staakk.progresstracker.data.exercise

import io.github.staakk.progresstracker.data.Id

data class Exercise(
    val id: Id = Id.Empty,
    val name: String,
) {

    companion object {
        val EmptyExercise = Exercise(name = "Unknown exercise")
    }
}