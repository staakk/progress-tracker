package io.github.staakk.ui.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.SearchField
import io.github.staakk.common.ui.compose.SimpleIconButton
import io.github.staakk.common.ui.compose.datetime.DateRangePickerDialog
import io.github.staakk.common.ui.compose.layout.StandardScreen
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.domain.training.TrainingPreviewData
import io.github.staakk.ui.trainings.TrainingsViewModel.Event.*
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Trainings(
    editTraining: (Id) -> Unit,
    navigateUp: () -> Unit,
) {
    val viewModel: TrainingsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(ScreenOpened)
    }
    val state by viewModel.state.collectAsState()
    state.newTrainingId?.let {
        LaunchedEffect(state.newTrainingId) {
            viewModel.dispatch(NewTrainingIdConsumed)
            editTraining(it)
        }
    }
    TrainingsScreen(
        state,
        viewModel::dispatch,
        editTraining,
        navigateUp,
    )
}

@Composable
fun TrainingsScreen(
    state: TrainingsState,
    dispatch: (TrainingsViewModel.Event) -> Unit,
    editTraining: (Id) -> Unit,
    navigateUp: () -> Unit,
) {
    StandardScreen(
        navigateUp = navigateUp,
        onFabClick = { dispatch(CreateNewTraining) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding),
        ) {
            SearchPanel(
                initialValue = state.exerciseQuery.orEmpty(),
                onExerciseQueryChanged = { dispatch(ExerciseQueryChanged(it)) },
                dateRange = state.dateQuery,
                onDateRangeChanged = { dispatch(DateQueryChanged(it)) }
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
                items(items = state.trainings, itemContent = { item ->
                    TrainingItem(
                        editTraining = editTraining,
                        training = item,
                    )
                })
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
        }
    }
}

@Composable
fun TrainingItem(
    editTraining: (Id) -> Unit,
    training: Training,
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = { editTraining(training.id) },
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            )
            .fillMaxWidth()
    ) {
        TrainingTitle(training = training)
        Spacer(modifier = Modifier.height(4.dp))
        training.rounds.map { it.exercise.name }
            .take(3)
            .joinToString(separator = ", ")
            .let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                )
            }
    }
}

@Composable
private fun TrainingTitle(training: Training) {
    Text(
        text = training.date.formatAsCardTitle(),
        style = MaterialTheme.typography.h6,
    )
}

@Composable
private fun LocalDateTime.formatAsCardTitle() = format(
    DateTimeFormatter.ofPattern(stringResource(R.string.trainings_training_card_title))
)

@Composable
private fun SearchPanel(
    initialValue: String,
    onExerciseQueryChanged: (String) -> Unit,
    dateRange: Pair<LocalDate, LocalDate>?,
    onDateRangeChanged: (Pair<LocalDate, LocalDate>) -> Unit,
) {
    Column {
        SearchField(
            modifier = Modifier
                .padding(16.dp),
            initialValue = initialValue,
            onValueChanged = onExerciseQueryChanged,
            hint = "Search by exercise name"
        )

        var showDateRangePicker: Boolean by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDateRangePicker = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            SimpleIconButton(
                onClick = { showDateRangePicker = true },
                imageVector = Icons.Outlined.EditCalendar,
                tint = MaterialTheme.colors.secondary,
                contentDescription = "Edit date",
            )

            val text = if (dateRange != null) {
                val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                dateRange.first.format(format) + " - " + dateRange.second.format(format)
            } else {
                "Click to select date"
            }

            Text(text = text)
        }

        Divider()

        DateRangePickerDialog(
            date = dateRange,
            show = showDateRangePicker,
            onDismiss = { showDateRangePicker = false },
            onDateRangeSelected = {
                Timber.e("$it")
                onDateRangeChanged(it)
            },
        )
    }
}

@Preview
@Composable
private fun PreviewTraining() {
    ProgressTrackerTheme {
        TrainingsScreen(
            state = TrainingsState(
                trainings = listOf(
                    TrainingPreviewData.training,
                    TrainingPreviewData.training,
                )
            ),
            dispatch = {},
            editTraining = {},
            navigateUp = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTrainingItem() {
    ProgressTrackerTheme {
        TrainingItem(
            editTraining = {},
            training = TrainingPreviewData.training,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchPanelPreview() {
    ProgressTrackerTheme {
        SearchPanel(
            initialValue = "",
            onExerciseQueryChanged = {},
            dateRange = null,
            onDateRangeChanged = {}
        )
    }
}