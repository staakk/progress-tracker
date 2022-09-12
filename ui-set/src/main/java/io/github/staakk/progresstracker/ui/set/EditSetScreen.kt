package io.github.staakk.progresstracker.ui.set

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.SimpleIconButton
import io.github.staakk.common.ui.compose.layout.StandardScreen
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.domain.training.TrainingPreviewData

@Composable
fun EditSet(
    setId: Id,
    navigateUp: () -> Unit,
) {
    val viewModel: EditSetViewModel = hiltViewModel()

    LaunchedEffect(viewModel, setId) {
        viewModel.dispatch(EditSetEvent.ScreenOpened(setId))
    }

    val state by viewModel.state.collectAsState()

    when (state) {
        is EditSetState.SetDeleted,
        is EditSetState.SetUpdated,
        -> LaunchedEffect(setId) {
            viewModel.dispatch(EditSetEvent.TerminalEventConsumed)
            navigateUp()
        }
        else -> EditSetScreen(
            state = state,
            dispatch = viewModel::dispatch,
        )
    }
}

@Composable
fun EditSetScreen(
    state: EditSetState,
    dispatch: (EditSetEvent) -> Unit,
) {
    var repsFieldValue by remember(state) {
        mutableStateOf(
            TextFieldValue(state.setOrNull()?.reps?.toString() ?: "")
        )
    }
    var weightFieldValue by remember(state) {
        mutableStateOf(
            TextFieldValue(state.setOrNull()?.weight?.toString() ?: "")
        )
    }
    StandardScreen(
        actionsStart = {
            SimpleIconButton(
                onClick = {
                    dispatch(
                        EditSetEvent.SaveSet(
                            reps = repsFieldValue.text,
                            weight = weightFieldValue.text,
                        )
                    )
                },
                imageVector = Icons.Outlined.Done,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )
        },
        actionsEnd = {
            SimpleIconButton(
                onClick = { dispatch(EditSetEvent.OpenDeleteDialog) },
                imageVector = Icons.Outlined.DeleteForever,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )
        }
    ) { padding ->
        if (state !is EditSetState.Loaded) return@StandardScreen

        Content(
            modifier = Modifier.padding(padding),
            repsFieldValue = repsFieldValue,
            onRepsValueChanged = {
                repsFieldValue = it.copy(text = it.text.filter(Char::isDigit))
            },
            weightFieldValue = weightFieldValue,
            onWeightValueChanged = {
                weightFieldValue = it.copy(text = it.text.filter(Char::isDigit))
            }
        )

        if (state.isDeleteDialogOpen()) {
            AlertDialog(
                onDismissRequest = { dispatch(EditSetEvent.CloseDeleteDialog) },
                title = { Text(text = "Delete set?") },
                text = { Text(text = "You're about to delete this set. This operation cannot be undone.") },
                confirmButton = {
                    Button(onClick = { dispatch(EditSetEvent.DeleteSet) }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { dispatch(EditSetEvent.CloseDeleteDialog) }) {
                        Text("Cancel")
                    }
                },
                properties = DialogProperties()
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    repsFieldValue: TextFieldValue,
    onRepsValueChanged: (TextFieldValue) -> Unit,
    weightFieldValue: TextFieldValue,
    onWeightValueChanged: (TextFieldValue) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = repsFieldValue,
            onValueChange = onRepsValueChanged,
            label = { Text("Reps") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = weightFieldValue,
            onValueChange = onWeightValueChanged,
            label = { Text("Weight") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview
@Composable
private fun PreviewEditSetScreen() {
    EditSetScreen(
        state = EditSetState.Loaded(
            set = TrainingPreviewData.set
        ),
        dispatch = {}
    )
}