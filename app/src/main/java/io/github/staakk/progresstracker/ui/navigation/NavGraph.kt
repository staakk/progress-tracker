package io.github.staakk.progresstracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.ui.exercise.EditExercise
import io.github.staakk.progresstracker.ui.navigation.Destinations.EXERCISE_ID_KEY
import io.github.staakk.progresstracker.ui.navigation.Destinations.ROUND_ID_KEY
import io.github.staakk.progresstracker.ui.exercise.search.ExercisesSearch
import io.github.staakk.progresstracker.ui.home.Home
import io.github.staakk.progresstracker.ui.navigation.Destinations.SetIdKey
import io.github.staakk.progresstracker.ui.navigation.Destinations.SetRoute
import io.github.staakk.progresstracker.ui.navigation.Destinations.TRAINING_ID_KEY
import io.github.staakk.progresstracker.ui.round.EditRound
import io.github.staakk.progresstracker.ui.set.EditSet
import io.github.staakk.ui.training.TrainingScreen
import io.github.staakk.ui.trainings.Trainings

object Destinations {
    const val EXERCISES_ROUTE = "exercises"
    const val EXERCISE_ROUTE = "exercise"
    const val EXERCISE_ID_KEY = "exercise_id"
    const val NEW_EXERCISE_ROUTE = "new_exercise"
    const val TRAININGS_ROUTE = "trainings"
    const val TRAINING_ROUTE = "training"
    const val TRAINING_ID_KEY = "training_id"
    const val ROUND_ROUTE = "round"
    const val ROUND_ID_KEY = "round_id"
    const val SetRoute = "set"
    const val SetIdKey = "set_id"
    const val HOME_ROUTE = "home"
}

@Composable
fun NavGraph(startDestination: String = Destinations.HOME_ROUTE) {
    val navController: NavHostController = rememberNavController()
    val actions: Actions = remember(navController) { Actions(navController) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.HOME_ROUTE) {
            Home(
                openExercisesList = actions.openExercisesList,
                openTrainings = actions.openTrainings
            )
        }

        composable(Destinations.EXERCISES_ROUTE) {
            ExercisesSearch(
                editExerciseAction = actions.editExercise,
                newExerciseAction = actions.newExercise
            )
        }

        composable(Destinations.TRAININGS_ROUTE) {
            Trainings(
                actions.editTraining,
                navigateUp = actions.navigateUp
            )
        }

        composable(
            "${Destinations.TRAINING_ROUTE}/{$TRAINING_ID_KEY}",
            arguments = listOf(navArgument(TRAINING_ID_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            TrainingScreen(
                id = Id.fromString(args.getString(TRAINING_ID_KEY))!!,
                editRound = actions.editRound,
                navigateUp = actions.navigateUp,
            )
        }

        composable(
            "${Destinations.EXERCISE_ROUTE}/{$EXERCISE_ID_KEY}",
            arguments = listOf(navArgument(EXERCISE_ID_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            EditExercise(
                exerciseId = Id.fromString(args.getString(EXERCISE_ID_KEY)),
                navigateUp = actions.navigateUp
            )
        }

        composable(Destinations.NEW_EXERCISE_ROUTE) {
            EditExercise(navigateUp = actions.navigateUp)
        }

        composable(
            "${Destinations.ROUND_ROUTE}/{$ROUND_ID_KEY}",
            arguments = listOf(navArgument(ROUND_ID_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            EditRound(
                roundId = Id.fromString(args.getString(ROUND_ID_KEY))!!,
                navigateUp = actions.navigateUp,
                editSet = actions.editSet,
            )
        }

        composable(
            route = "$SetRoute/{$SetIdKey}",
            arguments = listOf(navArgument(SetIdKey) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            EditSet(
                setId = Id.fromString(args.getString(SetIdKey))!!,
                navigateUp = actions.navigateUp,
            )
        }
    }
}

class Actions(navController: NavHostController) {
    val openExercisesList: () -> Unit = {
        navController.navigate(Destinations.EXERCISES_ROUTE)
    }
    val editExercise: (Id) -> Unit = { exerciseId ->
        navController.navigate("${Destinations.EXERCISE_ROUTE}/${exerciseId}")
    }
    val newExercise: () -> Unit = {
        navController.navigate(Destinations.NEW_EXERCISE_ROUTE)
    }
    val openTrainings: () -> Unit = {
        navController.navigate(Destinations.TRAININGS_ROUTE)
    }
    val editTraining: (Id) -> Unit = { trainingId ->
        navController.navigate("${Destinations.TRAINING_ROUTE}/${trainingId}")
    }
    val editRound: (Id) -> Unit = { roundId ->
        navController.navigate("${Destinations.ROUND_ROUTE}/${roundId}")
    }
    val editSet: (Id) -> Unit = { setId ->
        navController.navigate("$SetRoute/$setId")
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}