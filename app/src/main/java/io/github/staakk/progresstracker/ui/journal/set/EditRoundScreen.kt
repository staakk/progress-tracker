package io.github.staakk.progresstracker.ui.journal.set

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.AmbientFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundSet
import io.github.staakk.progresstracker.ui.common.Formatters
import io.github.staakk.progresstracker.ui.common.Header
import io.github.staakk.progresstracker.ui.common.SimpleIconButton
import io.github.staakk.progresstracker.ui.common.SuffixTransformation
import io.github.staakk.progresstracker.ui.theme.Dimensions
import io.github.staakk.progresstracker.ui.theme.ProgressTrackerTheme
import org.threeten.bp.LocalDate

@Composable
fun EditRound(
    navigateUp: () -> Unit,
    id: String? = null,
    date: LocalDate? = null,
) {
    val viewModel: EditRoundViewModel = viewModel()

    /* TODO Find different callback that will work with navigating up.
     * This needs to be improved. There should be no need for additional state for dismissal.
     */
    val roundDeleted = viewModel.roundDeleted.observeAsState(false)
    val dismissed = remember { mutableStateOf(false) }
    when {
        roundDeleted.value && !dismissed.value -> {
            onActive { navigateUp() }
            dismissed.value = true
            return
        }
        dismissed.value -> return
    }

    id?.let(viewModel::loadRound)
    date?.let(viewModel::createNewRound)

    EditRoundScreen(
        navigateUp = navigateUp,
        round = viewModel.round,
        exercises = viewModel.exercises,
        onExerciseSelected = viewModel::updateExercise,
        onSetUpdated = viewModel::updateSet,
        createSet = viewModel::createNewSet,
        deleteRound = viewModel::deleteCurrentRound,
        deleteSet = viewModel::deleteSet
    )
}

@Composable
fun EditRoundScreen(
    navigateUp: () -> Unit,
    round: LiveData<Round?>,
    exercises: LiveData<List<Exercise>>,
    onExerciseSelected: (Exercise) -> Unit,
    onSetUpdated: (RoundSet) -> Unit,
    createSet: () -> Unit,
    deleteRound: () -> Unit,
    deleteSet: (RoundSet) -> Unit,
) {
    ProgressTrackerTheme {
        Surface(Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bodyContent = {
                    BodyContent(
                        round = round,
                        exercises = exercises,
                        onExerciseSelected = onExerciseSelected,
                        onSetUpdated = onSetUpdated,
                        deleteSet = deleteSet,
                    )
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = createSet,
                    ) {
                        Icon(Icons.Filled.Add, tint = MaterialTheme.colors.onSecondary)
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        backgroundColor = MaterialTheme.colors.primary,
                        cutoutShape = CircleShape,
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                        ) {
                            val (deleteRef, navigateUpRef) = createRefs()
                            SimpleIconButton(
                                modifier = Modifier.constrainAs(navigateUpRef) {
                                    start.linkTo(parent.start)
                                },
                                onClick = navigateUp,
                                imageVector = Icons.Filled.ArrowBack,
                                tint = MaterialTheme.colors.onPrimary,
                            )
                            SimpleIconButton(
                                modifier = Modifier.constrainAs(deleteRef) {
                                    end.linkTo(parent.end)
                                },
                                onClick = deleteRound,
                                imageVector = Icons.Filled.DeleteForever,
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BodyContent(
    round: LiveData<Round?>,
    exercises: LiveData<List<Exercise>>,
    onExerciseSelected: (Exercise) -> Unit,
    onSetUpdated: (RoundSet) -> Unit,
    deleteSet: (RoundSet) -> Unit,
) {
    val roundState = round.observeAsState()
    val sets = roundState.value?.roundSets ?: emptyList()
    Column(modifier = Modifier.padding(16.dp)) {
        Header(text = roundState.value
            ?.createdAt
            ?.format(Formatters.DAY_MONTH_SHORT_YEAR_FORMATTER)
            ?: ""
        )
        ExerciseSelector(
            round = round,
            exercises = exercises,
            onExerciseSelected = onExerciseSelected
        )
        LazyColumn(
            modifier = Modifier.padding(top = Dimensions.padding),
            content = {
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // TODO Investigate how to get end padding - size of icon button in better way.
                            .padding(bottom = Dimensions.padding, end = 48.dp + Dimensions.padding),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(id = R.string.edit_set_reps_label),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(id = R.string.edit_set_weight_label),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                items(sets) { set ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimensions.padding),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
                    ) {
                        TextField(
                            value = set.reps.toString(),
                            onValueChange = {
                                onSetUpdated(set.copy(reps = it.toIntOrNull() ?: 0))
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        TextField(
                            visualTransformation = SuffixTransformation(AnnotatedString(
                                " kg",
                                SpanStyle(AmbientTextStyle.current.color.copy(alpha = 0.4f))
                            )),
                            value = set.weight.toString(),
                            onValueChange = {
                                onSetUpdated(set.copy(weight = it.toIntOrNull() ?: 0))
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        SimpleIconButton(
                            onClick = { deleteSet(set) },
                            imageVector = Icons.Filled.RemoveCircleOutline,
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }
            })
    }
}

@Composable
fun ExerciseSelector(
    round: LiveData<Round?>,
    exercises: LiveData<List<Exercise>>,
    onExerciseSelected: (Exercise) -> Unit,
) {
    val roundState = round.observeAsState()
    val itemsState = exercises.observeAsState(listOf())
    val items = itemsState.value
    val selectedIndex = remember {
        derivedStateOf {
            val exercise = roundState.value?.exercise ?: return@derivedStateOf 0
            return@derivedStateOf itemsState.value.indexOf(exercise)
        }
    }
    val expanded = remember { mutableStateOf(false) }
    val focusManager = AmbientFocusManager.current
    DropdownMenu(
        toggle = {
            TextField(
                value = if (items.isEmpty()) "" else items[selectedIndex.value].name,
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown) },
                modifier = Modifier
                    .onFocusChanged {
                        expanded.value = when (it) {
                            FocusState.Captured,
                            FocusState.Active,
                            -> true
                            FocusState.ActiveParent,
                            FocusState.Disabled,
                            FocusState.Inactive,
                            -> false
                        }
                    }
                    .fillMaxWidth(),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.edit_set_exercise_label)) },
            )
        },
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
            focusManager.clearFocus()
        },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { exercise ->
            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    focusManager.clearFocus()
                    onExerciseSelected(exercise)
                }
            ) {
                Text(text = exercise.name)
            }
        }
    }
}

@Preview
@Composable
fun PreviewEditSetScreen() {
    EditRoundScreen(
        {},
        round = MutableLiveData(null),
        MutableLiveData(
            listOf(
                Exercise(name = "Dead lift"),
                Exercise(name = "Dumbell row"),
                Exercise(name = "Barbell row"),
                Exercise(name = "Overhead press"),
                Exercise(name = "Bench press"),
            )
        ),
        {},
        {},
        {},
        {},
        {}
    )
}