package io.github.staakk.progresstracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import io.github.staakk.progresstracker.ui.exercise.ExercisesList
import io.github.staakk.progresstracker.ui.exercise.ExercisesListViewModel
import io.github.staakk.progresstracker.ui.exercise.editexercise.EditExercise
import io.github.staakk.progresstracker.ui.exercise.editexercise.EditExerciseViewModel
import io.github.staakk.progresstracker.ui.home.Home
import io.github.staakk.progresstracker.ui.journal.Journal
import io.github.staakk.progresstracker.ui.journal.JournalViewModel
import io.github.staakk.progresstracker.ui.journal.round.EditRound
import io.github.staakk.progresstracker.ui.journal.round.EditRoundViewModel
import io.github.staakk.progresstracker.ui.navigation.Destinations.EXERCISE_ID_KEY
import io.github.staakk.progresstracker.ui.navigation.Destinations.ROUND_CREATE_DATE_KEY
import io.github.staakk.progresstracker.ui.navigation.Destinations.ROUND_ID_KEY
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Destinations {
    const val EXERCISES_ROUTE = "exercises"
    const val EXERCISE_ROUTE = "exercise"
    const val EXERCISE_ID_KEY = "exerciseId"
    const val NEW_EXERCISE_ROUTE = "new_exercise"
    const val JOURNAL_ROUTE = "journal"
    const val ROUND_ROUTE = "round"
    const val ROUND_ID_KEY = "roundId"
    const val NEW_ROUND_ROUTE = "new_round"
    const val ROUND_CREATE_DATE_KEY = "date"
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
                openJournal = actions.openJournal
            )
        }
        composable(Destinations.EXERCISES_ROUTE) {
            val viewModel: ExercisesListViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            ExercisesList(
                viewModel = viewModel,
                editExerciseAction = actions.editExercise,
                newExerciseAction = actions.newExercise
            )
        }
        composable(Destinations.JOURNAL_ROUTE) {
            val viewModel: JournalViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            Journal(
                viewModel = viewModel,
                editSet = actions.editSet,
                newSet = actions.newSet,
                navigateUp = actions.upPress
            )
        }
        composable(
            "${Destinations.EXERCISE_ROUTE}/{$EXERCISE_ID_KEY}",
            arguments = listOf(navArgument(EXERCISE_ID_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            val viewModel: EditExerciseViewModel = viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            EditExercise(
                viewModel = viewModel,
                exerciseId = args.getString(EXERCISE_ID_KEY),
                navigateUp = actions.upPress
            )
        }
        composable(Destinations.NEW_EXERCISE_ROUTE) {
            val viewModel: EditExerciseViewModel = viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            EditExercise(viewModel = viewModel, navigateUp = actions.upPress)
        }
        composable(
            "${Destinations.NEW_ROUND_ROUTE}/{$ROUND_CREATE_DATE_KEY}",
            arguments = listOf(navArgument(ROUND_CREATE_DATE_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            val date =
                LocalDate.from(DateTimeFormatter.ISO_DATE.parse(args.getString(ROUND_CREATE_DATE_KEY)))
            val viewModel: EditRoundViewModel = viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            EditRound(viewModel = viewModel, navigateUp = actions.upPress, date = date)
        }
        composable(
            "${Destinations.ROUND_ROUTE}/{$ROUND_ID_KEY}",
            arguments = listOf(navArgument(ROUND_ID_KEY) { type = NavType.StringType })
        ) {
            val args = requireNotNull(it.arguments)
            val roundId = args.getString(ROUND_ID_KEY)
            val viewModel: EditRoundViewModel = viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            EditRound(
                viewModel = viewModel,
                navigateUp = actions.upPress,
                roundId = roundId
            )
        }
    }
}

class Actions(navController: NavHostController) {
    val openExercisesList: () -> Unit = {
        navController.navigate(Destinations.EXERCISES_ROUTE)
    }
    val editExercise: (String) -> Unit = { exerciseId ->
        navController.navigate("${Destinations.EXERCISE_ROUTE}/${exerciseId}")
    }
    val newExercise: () -> Unit = {
        navController.navigate(Destinations.NEW_EXERCISE_ROUTE)
    }
    val openJournal: () -> Unit = {
        navController.navigate(Destinations.JOURNAL_ROUTE)
    }
    val editSet: (String) -> Unit = { setId ->
        navController.navigate("${Destinations.ROUND_ROUTE}/${setId}")
    }
    val newSet: (LocalDate) -> Unit = { date ->
        navController.navigate("${Destinations.NEW_ROUND_ROUTE}/${date.format(DateTimeFormatter.ISO_DATE)}")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}