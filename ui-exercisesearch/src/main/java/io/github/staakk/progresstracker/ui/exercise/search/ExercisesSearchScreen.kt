package io.github.staakk.progresstracker.ui.exercise.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
            modifier = Modifier.testTag(ExercisesSearchTestTags.Search),
            initialValue = initialSearchValue,
            onValueChanged = onSearchValueChanged
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