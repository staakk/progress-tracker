package io.github.staakk.progresstracker.ui.exercise.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.staakk.common.ui.compose.SearchField
import io.github.staakk.common.ui.compose.testTag
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise

enum class ExercisesSearchTestTags {
    Search,
    ListItem,
    Fab
}

@Composable
fun ExercisesSearch(
    editExerciseAction: (Id) -> Unit,
    newExerciseAction: () -> Unit,
) {
    val viewModel: ExercisesSearchViewModel = hiltViewModel()
    val exercises: List<Exercise> by viewModel.exercises.collectAsState()
    ExerciseSearchScreen(
        exercises = exercises,
        onExerciseClick = editExerciseAction,
        onNewExerciseClick = newExerciseAction,
        onSearchValueChanged = viewModel::setSearchValue,
        initialSearchValue = viewModel.getSearchValue(),
    )
}

@Composable
private fun ExerciseSearchScreen(
    exercises: List<Exercise>,
    onExerciseClick: (Id) -> Unit,
    onNewExerciseClick: () -> Unit,
    onSearchValueChanged: (String) -> Unit,
    initialSearchValue: String,
) {
    Column {
        SearchField(
            modifier = Modifier
                .padding(16.dp)
                .testTag(ExercisesSearchTestTags.Search),
            initialValue = initialSearchValue,
            onValueChanged = onSearchValueChanged,
            hint = stringResource(id = R.string.exercises_list_search_label),
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(items = exercises, itemContent = {
                    ExerciseItem(
                        onItemClick = onExerciseClick,
                        exercise = it
                    )
                })
            }
            FloatingActionButton(
                onClick = onNewExerciseClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .testTag(ExercisesSearchTestTags.Fab),
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
private fun ExerciseItem(
    exercise: Exercise,
    onItemClick: (Id) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(exercise.id) })
            .testTag(ExercisesSearchTestTags.ListItem)
    ) {
        Text(
            text = exercise.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview
@Composable
private fun PreviewExerciseListScreenWithInput() {
    val exercises = listOf(
        Exercise(name = "Squat"),
        Exercise(name = "Dead Lift")
    )
    ProgressTrackerTheme {
        Surface {
            ExerciseSearchScreen(
                exercises = exercises,
                {},
                {},
                {},
                "Search",
            )
        }
    }
}

@Preview
@Composable
private fun PreviewExerciseListScreenWithoutInput() {
    val exercises = listOf(
        Exercise(name = "Squat"),
        Exercise(name = "Dead Lift")
    )
    ProgressTrackerTheme {
        Surface {
            ExerciseSearchScreen(
                exercises = exercises,
                {},
                {},
                {},
                "",
            )
        }
    }
}