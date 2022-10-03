package io.github.staakk.progresstracker.ui.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.Header
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.ui.exercise.EditExerciseViewModel.Event.*
import io.github.staakk.progresstracker.ui.exercise.EditExerciseScreenTags.NameField
import io.github.staakk.progresstracker.ui.exercise.EditExerciseScreenTags.ProgressIndicator

object EditExerciseScreenTags {
    const val ProgressIndicator = "ProgressIndicator"
    const val NameField = "NameField"
}

@Composable
fun EditExercise(
    exerciseId: Id? = null,
    navigateUp: () -> Unit,
) {
    val viewModel: EditExerciseViewModel = hiltViewModel()
    LaunchedEffect(viewModel, exerciseId) {
        with(viewModel) { dispatch(ScreenOpened(exerciseId)) }
    }
    val state by viewModel.state.collectAsState()
    EditExerciseScreen(
        state,
        { with(viewModel) { dispatch(it) } },
        navigateUp
    )
}

@Composable
internal fun EditExerciseScreen(
    state: EditExerciseState,
    dispatch: (EditExerciseViewModel.Event) -> Unit,
    navigateUp: () -> Unit,
) {
    when (state) {
        is EditExerciseState.Saved,
        is EditExerciseState.Error,
        -> SideEffect { navigateUp() }
        is EditExerciseState.Editing -> Editing(
            state,
            dispatch,
            navigateUp,
        )
        EditExerciseState.Loading,
        is EditExerciseState.Saving,
        -> CircularProgressIndicator(
            modifier = Modifier.testTag(ProgressIndicator)
        )
    }
}

@Composable
private fun Editing(
    state: EditExerciseState.Editing,
    dispatch: (EditExerciseViewModel.Event) -> Unit,
    navigateUp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Header(text = stringResource(state.headerText()))

        val name = state.exercise.name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(NameField),
            value = name,
            onValueChange = { dispatch(ExerciseNameChanged(it)) },
            label = { Text(stringResource(R.string.edit_exercise_label_name)) },
            enabled = true
        )

        Spacer(Modifier.weight(2f))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = navigateUp,
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(id = R.string.cancel)) }

            Button(
                onClick = { dispatch(SaveExerciseClicked) },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(id = R.string.done)) }
        }
    }
}

@Preview("Editing")
@Composable
private fun PreviewEditExerciseScreen() {
    EditExerciseScreen(
        state = EditExerciseState.Editing(false, Exercise(name = "Dead lift")),
        dispatch = {},
        navigateUp = {}
    )
}

private fun EditExerciseState.Editing.headerText() =
    if (isNewExercise) R.string.edit_exercise_header_create
    else R.string.edit_exercise_header_edit

