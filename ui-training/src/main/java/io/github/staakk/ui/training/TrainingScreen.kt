package io.github.staakk.ui.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.Training
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TrainingScreen(
    id: Id,
    editRound: (Id) -> Unit,
) {
    val viewModel: TrainingViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(TrainingEvent.LoadTraining(id))
    }

    val state by viewModel.state.collectAsState()
    Content(
        state,
        viewModel::dispatch,
        editRound,
    )
}

@Composable
private fun Content(
    state: TrainingState,
    dispatch: (TrainingEvent) -> Unit,
    editRound: (Id) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dispatch(TrainingEvent.CreateRound) },
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                style = MaterialTheme.typography.h5,
                text = state.dateFirstLine(),
            )
            Text(
                style = MaterialTheme.typography.subtitle1,
                text = state.dateSecondLine(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                itemsIndexed(items = state.getRounds()) { index, item ->
                    RoundItem(
                        onClick = { editRound(item.id) },
                        round = item
                    )
                    if (index != state.getRounds().lastIndex)
                        Divider()
                }
            }
        }
    }
}

@Composable
private fun RoundItem(onClick: () -> Unit, round: Round) {
    Column(
        modifier = Modifier
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
            append("kg")
        }
    }

    Text(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
        style = MaterialTheme.typography.body2,
        text = text
    )
}

private fun TrainingState.dateFirstLine() = when (this) {
    is TrainingState.Loaded -> training.date.format(DateTimeFormatter.ofPattern("EEEE HH:mm"))
    else -> ""
}

private fun TrainingState.dateSecondLine() = when (this) {
    is TrainingState.Loaded -> training.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    else -> ""
}

private fun TrainingState.getRounds(): List<Round> = when (this) {
    is TrainingState.Loaded -> training.rounds
    else -> emptyList()
}

@Preview
@Composable
private fun PreviewTrainingScreen() {
    ProgressTrackerTheme {
        Surface {
            Content(
                Training(
                    date = LocalDateTime.now(),
                    rounds = listOf(
                        Round(
                            ordinal = 1,
                            exercise = Exercise(name = "Exercise name"),
                            roundSets = listOf(
                                RoundSet(
                                    ordinal = 1,
                                    reps = 2,
                                    weight = 50
                                ),
                                RoundSet(
                                    ordinal = 1,
                                    reps = 2,
                                    weight = 50
                                ),
                            )
                        ),
                        Round(
                            ordinal = 1,
                            exercise = Exercise(name = "Exercise name"),
                            roundSets = listOf(
                                RoundSet(
                                    ordinal = 1,
                                    reps = 2,
                                    weight = 50
                                ),
                                RoundSet(
                                    ordinal = 1,
                                    reps = 2,
                                    weight = 50
                                ),
                            )
                        )
                    )
                ).let { TrainingState.Loaded(it) },
                dispatch = {},
                editRound = {},
            )
        }
    }
}