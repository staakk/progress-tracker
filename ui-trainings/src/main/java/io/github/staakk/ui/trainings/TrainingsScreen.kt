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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.Training
import java.time.LocalDateTime

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
    Column {
        Box(modifier = Modifier.fillMaxSize()) {
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
            FloatingActionButton(
                onClick = { dispatch(TrainingsEvent.CreateNewTraining) },
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
}

@Composable
fun TrainingItem(onClick: () -> Unit, training: Training) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            )
        ) {
            Text(text = "${training.date}")
        }
    }
}

@Preview
@Composable
private fun PreviewTraining() {
    ProgressTrackerTheme {
        Surface {
            TrainingsScreen(
                trainings = listOf(
                    Training(date = LocalDateTime.now())
                ),
                dispatch = {},
                editTraining = {}
            )
        }
    }
}
