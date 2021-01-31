package io.github.staakk.progresstracker.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate

sealed class Destination: Parcelable {
    @Parcelize
    object ExercisesList: Destination()

    @Parcelize
    data class EditExercise(val exerciseId: String): Destination()

    @Parcelize
    object NewExercise: Destination()

    @Parcelize
    object OpenJournal : Destination()

    @Parcelize
    data class EditSet(val setId: String) : Destination()

    @Parcelize
    data class NewSet(val date: LocalDate) : Destination()

    @Parcelize
    object Home: Destination()
}

class Actions(navigator: Navigator<Destination>) {
    val openExercisesList: () -> Unit = {
        navigator.navigate(Destination.ExercisesList)
    }
    val editExercise: (exerciseId: String) -> Unit = {
        navigator.navigate(Destination.EditExercise(it))
    }
    val newExercise: () -> Unit = {
        navigator.navigate(Destination.NewExercise)
    }
    val openJournal: () -> Unit = {
        navigator.navigate(Destination.OpenJournal)
    }
    val editSet: (id: String) -> Unit = {
        navigator.navigate(Destination.EditSet(it))
    }
    val newSet: (date: LocalDate) -> Unit = {
        navigator.navigate(Destination.NewSet(it))
    }
    val upPress: () -> Unit = {
        navigator.back()
    }
}