package io.github.staakk.ui.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.Header
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.Training
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Trainings(
    editTraining: (Id) -> Unit,
) {
    val viewModel: TrainingsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(TrainingsEvent.ScreenOpened)
    }
    val trainings: List<Training> by viewModel.trainings.collectAsState()
    TrainingsScreen(
        trainings,
        viewModel::dispatch,
        editTraining,
    )
}

@Composable
fun TrainingsScreen(
    trainings: List<Training>,
    dispatch: (TrainingsEvent) -> Unit,
    editTraining: (Id) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dispatch(TrainingsEvent.CreateNewTraining) },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    Icons.Filled.Add,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = stringResource(id = R.string.exercises_list_content_desc_add_new_exercise),
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            Header(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 16.dp),
                text = stringResource(R.string.trainings_title))
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = trainings, itemContent = {
                TrainingItem(
                    onClick = { editTraining(it.id) },
                    training = it
                )
            })
        }

    }
}

@Composable
fun TrainingItem(onClick: () -> Unit, training: Training) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                    indication = rememberRipple(),
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = training.date.formatAsCardTitle(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                training.rounds.forEach {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        text = it.summaryText()
                    )
                }
            }
        }
    }
}

private fun LocalDateTime.formatAsCardTitle() = format(
    DateTimeFormatter.ofPattern("EEEE HH:mm, dd MMM yyyy")
)

@Composable
private fun Round.summaryText() = buildAnnotatedString {
    append(exercise.name)
    append(' ')
    withStyle(SpanStyle(Color.Gray)) {
        append(roundSets.count().toString())
        append(" sets")
    }
}

@Preview
@Composable
private fun PreviewTraining() {
    ProgressTrackerTheme {
        Surface {
            TrainingsScreen(
                trainings = listOf(
                    Training(
                        date = LocalDateTime.now(),
                        rounds = listOf(
                            Round(
                                ordinal = 1,
                                exercise = Exercise(name = "Dead lift"),
                                roundSets = listOf(
                                    RoundSet(ordinal = 1, reps = 2, weight = 3),
                                    RoundSet(ordinal = 1, reps = 2, weight = 3),
                                )
                            ),
                            Round(
                                ordinal = 1,
                                exercise = Exercise(name = "Squat"),
                                roundSets = listOf(
                                    RoundSet(ordinal = 1, reps = 2, weight = 3),
                                    RoundSet(ordinal = 1, reps = 2, weight = 3),
                                )
                            )
                        )
                    )
                ),
                dispatch = {},
                editTraining = {}
            )
        }
    }
}
