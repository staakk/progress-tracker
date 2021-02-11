package io.github.staakk.progresstracker.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.staakk.progresstracker.ui.exercise.ExercisesList
import io.github.staakk.progresstracker.ui.exercise.editexercise.EditExercise
import io.github.staakk.progresstracker.ui.home.Home
import io.github.staakk.progresstracker.ui.journal.Journal
import io.github.staakk.progresstracker.ui.journal.round.EditRound
import io.github.staakk.progresstracker.ui.navigation.Actions
import io.github.staakk.progresstracker.ui.navigation.LocalBackDispatcher
import io.github.staakk.progresstracker.ui.navigation.Destination
import io.github.staakk.progresstracker.ui.navigation.Navigator

@Composable
fun ProgressTrackerApp(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSaveable(
        saver = Navigator.saver(backDispatcher)
    ) {
        Navigator(Destination.Home, backDispatcher)
    }

    val actions = remember(navigator) { Actions(navigator) }

    Providers(LocalBackDispatcher provides backDispatcher) {
        Crossfade(navigator.current) {
            when (it) {
                Destination.Home -> Home(
                    actions.openExercisesList,
                    actions.openJournal
                )
                Destination.ExercisesList -> ExercisesList(
                    actions.editExercise,
                    actions.newExercise
                )
                Destination.NewExercise -> EditExercise(
                    navigateUp = actions.upPress
                )
                is Destination.EditExercise -> EditExercise(
                    it.exerciseId,
                    actions.upPress
                )
                Destination.OpenJournal -> Journal(
                    actions.editSet,
                    actions.newSet,
                    navigateUp = actions.upPress,
                )
                is Destination.EditSet -> EditRound(
                    navigateUp = actions.upPress,
                    id = it.setId,
                )
                is Destination.NewSet -> EditRound(
                    navigateUp = actions.upPress,
                    date = it.date
                )
            }
        }
    }
}