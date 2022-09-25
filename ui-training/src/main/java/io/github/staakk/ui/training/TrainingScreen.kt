package io.github.staakk.ui.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.SimpleIconButton
import io.github.staakk.common.ui.compose.datetime.DatePickerDialog
import io.github.staakk.common.ui.compose.datetime.TimePickerDialog
import io.github.staakk.common.ui.compose.deletedialog.DeletePermanentlyDialog
import io.github.staakk.common.ui.compose.deletedialog.DialogState
import io.github.staakk.common.ui.compose.layout.StandardScreen
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.common.time.TimeOfDay
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.domain.training.TrainingPreviewData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TrainingScreen(
    id: Id,
    editRound: (Id) -> Unit,
    navigateUp: () -> Unit,
) {
    val viewModel: TrainingViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(TrainingEvent.LoadTraining(id))
    }

    val state by viewModel.state.collectAsState()
    if (state.trainingDeleted) {
        LaunchedEffect(id) {
            viewModel.dispatch(TrainingEvent.DeleteTrainingConsumed)
            navigateUp()
        }
    }

    state.let {
        if (it.newRoundId != null) {
            LaunchedEffect(it.newRoundId) {
                viewModel.dispatch(TrainingEvent.NewRoundIdConsumed)
                editRound(it.newRoundId)
            }
        }
    }

    Content(
        state,
        viewModel::dispatch,
        editRound,
        navigateUp,
    )
}

@Composable
private fun Content(
    state: TrainingState,
    dispatch: (TrainingEvent) -> Unit,
    editRound: (Id) -> Unit,
    navigateUp: () -> Unit,
) {
    StandardScreen(
        navigateUp = navigateUp,
        onFabClick = { dispatch(TrainingEvent.CreateRound) },
        actionsEnd = {
            SimpleIconButton(
                onClick = { dispatch(TrainingEvent.OpenDeleteDialog) },
                imageVector = Icons.Outlined.DeleteForever,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )
        }
    ) {
        Box {
            var showDatePicker: Boolean by remember { mutableStateOf(false) }
            var showTimePicker: Boolean by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                SimpleIconButton(
                    onClick = { showDatePicker = true },
                    imageVector = Icons.Outlined.EditCalendar,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Edit date",
                )
                SimpleIconButton(
                    onClick = { showTimePicker = true },
                    imageVector = Icons.Outlined.AccessTime,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Edit time"
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column {
                        Text(
                            style = MaterialTheme.typography.h5,
                            text = state.dateFirstLine(),
                        )
                        Text(
                            style = MaterialTheme.typography.subtitle1,
                            text = state.dateSecondLine(),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    itemsIndexed(items = state.getRounds()) { index, item ->
                        RoundItem(
                            onClick = { editRound(item.id) },
                            round = item
                        )
                        if (index != state.getRounds().lastIndex) Divider()
                    }
                }

                DatePickerDialog(
                    date = state.trainingDate(),
                    show = showDatePicker,
                    onDismiss = { showDatePicker = false },
                    onDateSelected = { date ->
                        dispatch(TrainingEvent.UpdateTrainingDate(date))
                    }
                )

                TimePickerDialog(
                    time = state.trainingTime(),
                    show = showTimePicker,
                    onDismiss = { showTimePicker = false },
                    onTimeSelected = { time ->
                        dispatch(TrainingEvent.UpdateTrainingTime(time))
                    }
                )

                DeletePermanentlyDialog(
                    dialogState = state.dialogState,
                    title = stringResource(R.string.training_delete_dialog_title),
                    onDismiss = { dispatch(TrainingEvent.CloseDeleteDialog) },
                    onConfirm = { dispatch(TrainingEvent.DeleteTraining) },
                )
            }
        }
    }
}

@Composable
private fun RoundItem(onClick: () -> Unit, round: Round) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.body1,
            text = round.exercise.name
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(round.roundSets) { RoundSetItem(roundSet = it) }
        }
    }
}

@Composable
private fun RoundSetItem(roundSet: RoundSet) {
    val textColorWithAlpha = LocalTextStyle.current.color.copy(alpha = 0.4f)
    val text = buildAnnotatedString {
        append(roundSet.reps.toString())
        append(' ')
        withStyle(SpanStyle(textColorWithAlpha)) {
            append('x')
        }
        append(' ')
        append(roundSet.weight.toString())
        withStyle(SpanStyle(textColorWithAlpha)) {
            append(stringResource(R.string.common_weight_kg))
        }
    }

    Text(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
        style = MaterialTheme.typography.body2,
        text = text
    )
}

@Composable
private fun TrainingState.dateFirstLine() = training
    ?.date
    ?.format(DateTimeFormatter.ofPattern(stringResource(R.string.training_date_first_line_format)))
    ?: ""

@Composable
private fun TrainingState.dateSecondLine() = training
    ?.date
    ?.format(DateTimeFormatter.ofPattern(stringResource(R.string.training_date_second_line_format)))
    ?: ""

private fun TrainingState.getRounds(): List<Round> = training
    ?.rounds
    ?: emptyList()

private fun TrainingState.trainingDate(): LocalDate? = training
    ?.date
    ?.toLocalDate()

private fun TrainingState.trainingTime(): TimeOfDay? = training
    ?.date
    ?.let { TimeOfDay(it.hour, it.minute) }

@Preview
@Composable
private fun PreviewTrainingScreen() {
    ProgressTrackerTheme {
        Surface {
            Content(
                TrainingState(
                    training = TrainingPreviewData.training,
                    dialogState = DialogState.Closed
                ),
                dispatch = {},
                editRound = {},
                navigateUp = {}
            )
        }
    }
}