package io.github.staakk.ui.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = state.dateText())

            LazyColumn {
                items(state.getRounds()) {
                    RoundItem(
                        onClick = { editRound(it.id) },
                        round = it
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { dispatch(TrainingEvent.CreateRound) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                Icons.Filled.Add,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = stringResource(id = R.string.exercises_list_content_desc_add_new_exercise),
            )
        }
    }
}

@Composable
private fun RoundItem(onClick: () -> Unit, round: Round) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(round.exercise.name + " exercise")

        Row {
            round.roundSets.forEach {
                Text("${it.reps} x ${it.weight}g")
            }
        }
    }
}

private fun TrainingState.dateText() = when (this) {
    is TrainingState.Loaded -> training.date.toString()
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
                        )
                    )
                ).let { TrainingState.Loaded(it) },
                dispatch = {},
                editRound = {},
            )
        }
    }
}