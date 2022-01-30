package io.github.staakk.progresstracker.ui.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.staakk.common.ui.compose.Header
import io.github.staakk.common.ui.compose.LoadingIndicator
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.ui.exercise.EditExerciseViewModel.ErrorType
import io.github.staakk.progresstracker.ui.exercise.EditExerciseViewModel.ScreenState
import io.github.staakk.common.ui.compose.testTag

enum class EditExerciseScreenTags {
    NAME
}

@Composable
fun EditExercise(
    exerciseId: String? = null,
    navigateUp: () -> Unit
) {
    val viewModel: EditExerciseViewModel = hiltViewModel()
    viewModel.loadExercise(exerciseId)
    EditExerciseScreen(
        exerciseId == null,
        viewModel.screenState,
        viewModel.exerciseName,
        viewModel::setExerciseName,
        viewModel::saveExercise,
        navigateUp
    )
}

@Composable
fun EditExerciseScreen(
    isNewExercise: Boolean,
    screenState: LiveData<ScreenState>,
    exerciseName: LiveData<String>,
    onNameChanged: (String) -> Unit,
    saveExercise: () -> Unit,
    navigateUp: () -> Unit
) {
    ProgressTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            val state = screenState.observeAsState(ScreenState.Editing).value
            if (state == ScreenState.Saved) {
                SideEffect { navigateUp() }
                return@Surface
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                Header(text = stringResource(getHeaderText(isNewExercise)))

                val name = exerciseName.observeAsState(initial = "")
                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .testTag(EditExerciseScreenTags.NAME),
                    value = name.value,
                    onValueChange = { onNameChanged(it) },
                    label = { Text(stringResource(R.string.edit_exercise_label_name)) },
                    enabled = state.isInteractive
                )

                if (state is ScreenState.Error) {
                    Text(
                        text = stringResource(getErrorText(state.type)),
                        modifier = Modifier.padding(top = 16.dp),
                        color = MaterialTheme.colors.error,
                    )
                }

                Spacer(modifier = Modifier.weight(2f))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                    OutlinedButton(
                        onClick = navigateUp,
                        modifier = Modifier.weight(1f),
                        enabled = state.isInteractive
                    ) { Text(stringResource(id = R.string.cancel)) }

                    Button(
                        onClick = { saveExercise() },
                        modifier = Modifier.weight(1f),
                        enabled = state.isInteractive
                    ) {
                        if (state.isInteractive) {
                            Text(
                                text = stringResource(id = R.string.done),
                                style = MaterialTheme.typography.button
                            )
                        } else {
                            LoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Preview("Editing")
@Composable
fun PreviewEditExerciseScreen() {
    EditExerciseScreen(
        false,
        MutableLiveData(ScreenState.Editing),
        MutableLiveData("DeadLift"),
        {},
        {},
        {},
    )
}

private fun getHeaderText(isNewExercise: Boolean) = if (isNewExercise) {
    R.string.edit_exercise_header_create
} else {
    R.string.edit_exercise_header_edit
}

private fun getErrorText(type: ErrorType) = when (type) {
    ErrorType.NameAlreadyExists -> R.string.edit_exercise_error_name_already_exists
    ErrorType.UnknownExerciseId -> R.string.edit_exercise_error_unknown_exercise
}

