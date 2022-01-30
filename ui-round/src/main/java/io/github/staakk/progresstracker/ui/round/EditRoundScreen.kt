package io.github.staakk.progresstracker.ui.round

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.staakk.common.ui.compose.*
import io.github.staakk.common.ui.compose.effect.OnceEffect
import io.github.staakk.common.ui.compose.theme.Dimensions
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.data.round.RoundSet
import java.time.LocalDate

enum class EditRoundTags {
    EDIT_REPS,
    EDIT_WEIGHT,
    EXERCISE_DROP_DOWN,
    EXERCISE_DROP_DOWN_ITEM,
    BACK,
    DELETE_ROUND,
    DELETE_SET,
    ADD_SET,
    SET,
}

@Composable
fun EditRound(
    navigateUp: () -> Unit,
    roundId: String? = null,
    date: LocalDate? = null,
) {
    val viewModel: EditRoundViewModel = hiltViewModel()
    val roundDeleted = viewModel.roundDeleted.observeAsState(false)
    if (roundDeleted.value) {
        SideEffect { navigateUp() }
        return
    }

    OnceEffect(roundId, date) {
        date?.let(viewModel::createNewRound)
        roundId?.let(viewModel::loadRound)
    }

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
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.testTag(EditRoundTags.ADD_SET),
                        onClick = createSet,
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            tint = MaterialTheme.colors.onSecondary,
                            contentDescription = stringResource(id = R.string.edit_round_content_desc_fab_add_set)
                        )
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
                                modifier = Modifier
                                    .testTag(EditRoundTags.BACK)
                                    .constrainAs(navigateUpRef) { start.linkTo(parent.start) },
                                onClick = navigateUp,
                                imageVector = Icons.Filled.ArrowBack,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = stringResource(id = R.string.edit_round_content_desc_go_back)
                            )
                            SimpleIconButton(
                                modifier = Modifier
                                    .testTag(EditRoundTags.DELETE_ROUND)
                                    .constrainAs(deleteRef) { end.linkTo(parent.end) },
                                onClick = deleteRound,
                                imageVector = Icons.Filled.DeleteForever,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = stringResource(id = R.string.edit_round_content_desc_delete_round)
                            )
                        }
                    }
                }
            ) {
                BodyContent(
                    round = round,
                    exercises = exercises,
                    onExerciseSelected = onExerciseSelected,
                    onSetUpdated = onSetUpdated,
                    deleteSet = deleteSet,
                )
            }
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
                            .testTag(EditRoundTags.SET)
                            .fillMaxWidth()
                            .padding(bottom = Dimensions.padding),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
                    ) {
                        TextField(
                            value = set.reps.toString(),
                            onValueChange = {
                                onSetUpdated(set.copy(reps = it.toIntOrNull() ?: 0))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag(EditRoundTags.EDIT_REPS),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        TextField(
                            visualTransformation = SuffixTransformation(AnnotatedString(
                                " kg",
                                SpanStyle(LocalTextStyle.current.color.copy(alpha = 0.4f))
                            )),
                            value = set.weight.toString(),
                            onValueChange = {
                                onSetUpdated(set.copy(weight = it.toIntOrNull() ?: 0))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag(EditRoundTags.EDIT_WEIGHT),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        SimpleIconButton(
                            modifier = Modifier.testTag(EditRoundTags.DELETE_SET),
                            onClick = { deleteSet(set) },
                            imageVector = Icons.Filled.RemoveCircleOutline,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = stringResource(id = R.string.edit_round_content_desc_delete_set)
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
    val focusManager = LocalFocusManager.current
    Box {
        OutlinedTextField(
            modifier = Modifier
                .testTag(EditRoundTags.EXERCISE_DROP_DOWN)
                .clickable { expanded.value = !expanded.value }
                .focusable(false)
                .onFocusChanged {
                    expanded.value = it.isFocused
                }
                .fillMaxWidth(),
            value = if (items.isEmpty()) "" else items[selectedIndex.value].name,
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown,
                    stringResource(id = R.string.edit_round_content_desc_expand_exercises_drop_down))
            },
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.edit_set_exercise_label)) },
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
                focusManager.clearFocus()
            },
        ) {
            items.forEach { exercise ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(EditRoundTags.EXERCISE_DROP_DOWN_ITEM),
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
                Exercise(name = "Dumbbell row"),
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