package io.github.staakk.progresstracker.ui.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.ui.theme.ProgressTrackerTheme

@Composable
fun ExercisesList(
    editExerciseAction: (String) -> Unit,
    newExerciseAction: () -> Unit,
) {
    val viewModel: ExercisesListViewModel = viewModel(
        ExercisesListViewModel::class.simpleName
    )
    viewModel.setSearchValue(viewModel.getSearchValue())
    ExerciseListScreen(
        exercises = viewModel.exercises,
        onExerciseClick = editExerciseAction,
        onNewExerciseClick = newExerciseAction,
        onSearchValueChanged = viewModel::setSearchValue,
        initialSearchValue = viewModel.getSearchValue(),
    )
}

@Composable
private fun ExerciseListScreen(
    exercises: LiveData<List<Exercise>>,
    onExerciseClick: (String) -> Unit,
    onNewExerciseClick: () -> Unit,
    onSearchValueChanged: (String) -> Unit,
    initialSearchValue: String,
) {
    ProgressTrackerTheme {
        Surface(Modifier.fillMaxSize()) {
            Column {
                SearchView(initialSearchValue, onSearchValueChanged)
                Box(modifier = Modifier.fillMaxSize()) {
                    val items = exercises.observeAsState(listOf())
                    LazyColumn {
                        items(items = items.value, itemContent = {
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
                            .padding(16.dp),
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Icon(Icons.Filled.Add, tint = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewExerciseListScreen() {
    val exercises = listOf(
        Exercise(name = "Squat"),
        Exercise(name = "Dead Lift")
    )
    ExerciseListScreen(
        exercises = MutableLiveData(exercises),
        {},
        {},
        {},
        "Search",
    )
}

@Composable
private fun ExerciseItem(
    exercise: Exercise,
    onItemClick: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(exercise.id) })
    ) {
        Text(
            text = exercise.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun SearchView(initialSearchValue: String, onValueChanged: (String) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(initialSearchValue)) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        leadingIcon = { Icon(Icons.Filled.Search) },
        onValueChange = {
            text = it
            onValueChanged(it.text)
        },
        placeholder = { Text(stringResource(id = R.string.exercises_list_search_label)) }
    )
}