package io.github.staakk.ui.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.layout.StandardScreen
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
    navigateUp: () -> Unit,
) {
    val viewModel: TrainingsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(TrainingsEvent.ScreenOpened)
    }
    val state by viewModel.trainings.collectAsState()
    state.newTrainingId?.let {
        LaunchedEffect(state.newTrainingId) {
            viewModel.dispatch(TrainingsEvent.NewTrainingIdConsumed)
            editTraining(it)
        }
    }
    TrainingsScreen(
        state.trainings,
        viewModel::dispatch,
        editTraining,
        navigateUp,
    )
}

@Composable
fun TrainingsScreen(
    trainings: List<Training>,
    dispatch: (TrainingsEvent) -> Unit,
    editTraining: (Id) -> Unit,
    navigateUp: () -> Unit,
) {
    StandardScreen(
        navigateUp = navigateUp,
        onFabClick = { dispatch(TrainingsEvent.CreateNewTraining) }
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items = trainings, itemContent = {
                TrainingItem(
                    editTraining = editTraining,
                    training = it
                )
            })
        }

    }
}

@Composable
fun TrainingItem(
    editTraining: (Id) -> Unit,
    training: Training,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { editTraining(training.id) },
                    indication = rememberRipple(),
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TrainingTitle(training = training)

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

@Composable
private fun TrainingTitle(training: Training) {
    Text(
        modifier = Modifier.padding(bottom = 16.dp),
        text = training.date.formatAsCardTitle(),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
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
                editTraining = {},
                navigateUp = {}
            )
        }
    }
}
