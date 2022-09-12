package io.github.staakk.progresstracker.ui.round

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.*
import io.github.staakk.common.ui.compose.deletedialog.DeletePermanentlyDialog
import io.github.staakk.common.ui.compose.layout.StandardScreen
import io.github.staakk.common.ui.compose.theme.Dimensions
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.domain.training.TrainingPreviewData

@Composable
fun EditRound(
    roundId: Id,
    navigateUp: () -> Unit,
    editSet: (Id) -> Unit,
) {
    val viewModel: EditRoundViewModel = hiltViewModel()

    LaunchedEffect(viewModel, roundId) {
        viewModel.dispatch(EditRoundEvent.ScreenOpened(roundId))
    }

    val state by viewModel.state.collectAsState()

    state.newSetId
        ?.let { id ->
            LaunchedEffect(id) {
                viewModel.dispatch(EditRoundEvent.NewSetIdConsumed)
                editSet(id)
            }
        }

    if (state.roundDeleted) {
        LaunchedEffect(roundId) {
            viewModel.dispatch(EditRoundEvent.DeleteRoundConsumed)
            navigateUp()
        }
    }

    EditRoundScreen(
        navigateUp = navigateUp,
        state = state,
        dispatch = viewModel::dispatch,
        editSet = editSet,
    )
}

@Composable
private fun EditRoundScreen(
    navigateUp: () -> Unit,
    state: EditRoundState,
    dispatch: (EditRoundEvent) -> Unit,
    editSet: (Id) -> Unit,
) {
    StandardScreen(
        navigateUp = navigateUp,
        onFabClick = { dispatch(EditRoundEvent.CreateSet) },
        actionsEnd = {
            SimpleIconButton(
                onClick = { dispatch(EditRoundEvent.OpenDeleteDialog) },
                imageVector = Icons.Outlined.DeleteForever,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            state = state,
            dispatch = dispatch,
            editSet = editSet,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    state: EditRoundState,
    dispatch: (EditRoundEvent) -> Unit,
    editSet: (Id) -> Unit,
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .padding(16.dp)
    ) {
        ExerciseSelector(
            state = state,
            onExerciseSelected = { dispatch(EditRoundEvent.UpdateExercise(it)) }
        )
        LazyColumn(
            modifier = Modifier.padding(top = Dimensions.padding),
        ) {
            items(state.roundSets) { set ->
                SetItem(
                    set = set,
                    editSet = editSet,
                )
            }
        }

        DeletePermanentlyDialog(
            dialogState = state.deleteDialogState,
            title = stringResource(R.string.edit_round_delete_dialog_title),
            onDismiss = { dispatch(EditRoundEvent.CloseDeleteDialog) },
            onConfirm = { dispatch(EditRoundEvent.DeleteRound) },
        )
    }
}

@Composable
private fun SetItem(
    set: RoundSet,
    editSet: (Id) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.padding),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = set.reps.toString(),
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = set.weight.toString() + " kg",
            modifier = Modifier
                .weight(1f),
        )
        SimpleIconButton(
            onClick = { editSet(set.id) },
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit set"
        )
    }
}

@Composable
private fun ExerciseSelector(
    state: EditRoundState,
    onExerciseSelected: (Exercise) -> Unit,
) {
    val selectedIndex = state.selectedExerciseIndex
    val exercises = state.exercises

    val expanded = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Box {
        OutlinedTextField(
            modifier = Modifier
                .clickable { expanded.value = !expanded.value }
                .focusable(false)
                .onFocusChanged { expanded.value = it.isFocused }
                .fillMaxWidth(),
            value =
            if (exercises.isEmpty() || selectedIndex == -1) ""
            else exercises[selectedIndex].name,
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown,
                    stringResource(id = R.string.edit_round_content_desc_expand_exercises_drop_down))
            },
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.edit_set_exercise_label)) },
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
                focusManager.clearFocus()
            },
        ) {
            state.exercises.forEach { exercise ->
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        focusManager.clearFocus()
                        onExerciseSelected(exercise)
                    }
                ) {
                    Text(text = exercise.name)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewEditSetScreen() {
    EditRoundScreen(
        state = EditRoundState(
            round = TrainingPreviewData.round,
            exercises = TrainingPreviewData.exercises,
        ),
        navigateUp = {},
        dispatch = {},
        editSet = {},
    )
}