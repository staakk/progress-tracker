package io.github.staakk.progresstracker.ui.journal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.ui.common.Calendar
import io.github.staakk.progresstracker.ui.common.Formatters
import io.github.staakk.progresstracker.ui.common.Selection
import io.github.staakk.progresstracker.ui.common.SimpleIconButton
import io.github.staakk.progresstracker.ui.theme.Dimensions
import io.github.staakk.progresstracker.ui.theme.ProgressTrackerTheme
import io.github.staakk.progresstracker.util.datetime.AmbientDateTimeProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

enum class JournalTestTags {
    CALENDAR,
    CALENDAR_TOGGLE_BUTTON,
    PREV_DAY,
    NEXT_DAY,
}

@Composable
fun Journal(
    editSet: (String) -> Unit,
    newSet: (LocalDate) -> Unit,
    navigateUp: () -> Unit,
) {
    val viewModel: JournalViewModel = viewModel()
    val dateTimeProvider = AmbientDateTimeProvider.current
    viewModel.loadRounds(viewModel.date.value ?: dateTimeProvider.currentDate())
    viewModel.loadMonth(viewModel.date.value?.let(YearMonth::from)
        ?: dateTimeProvider.currentYearMonth())
    JournalScreen(
        date = viewModel.date,
        rounds = viewModel.rounds,
        daysWithRound = viewModel.daysWithRound,
        editSet = editSet,
        newSet = newSet,
        navigateUp = navigateUp,
        onDateChanged = viewModel::loadRounds,
        onMonthChanged = viewModel::loadMonth
    )
}

@Composable
fun JournalScreen(
    navigateUp: () -> Unit,
    date: LiveData<LocalDate>,
    rounds: LiveData<List<Round>>,
    daysWithRound: LiveData<List<LocalDate>>,
    editSet: (String) -> Unit,
    newSet: (LocalDate) -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
) {
    val dateState = date.observeAsState(AmbientDateTimeProvider.current.currentDate())
    ProgressTrackerTheme {
        Surface(Modifier.fillMaxSize()) {
            Scaffold(
                bodyContent = {
                    BodyContent(
                        date = date,
                        rounds = rounds,
                        daysWithRound = daysWithRound,
                        editSet = editSet,
                        onDateChanged = onDateChanged,
                        onMonthChanged = onMonthChanged,
                    )
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
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
                            val (navigateUpRef) = createRefs()
                            SimpleIconButton(
                                modifier = Modifier.constrainAs(navigateUpRef) {
                                    start.linkTo(parent.start)
                                },
                                onClick = navigateUp,
                                imageVector = Icons.Filled.ArrowBack,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = stringResource(id = R.string.journal_content_desc_go_back)
                            )
                        }
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { newSet(dateState.value) },
                        text = {
                            Text(
                                text = stringResource(R.string.journal_add_round),
                                style = MaterialTheme.typography.button
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                tint = MaterialTheme.colors.onSecondary,
                                contentDescription = null,
                            )
                        },
                    )
                }
            )
        }
    }
}

@Composable
private fun BodyContent(
    date: LiveData<LocalDate>,
    rounds: LiveData<List<Round>>,
    daysWithRound: LiveData<List<LocalDate>>,
    editSet: (String) -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
) {
    val selectionColor = MaterialTheme.colors.primary
    val dateState = date.observeAsState(AmbientDateTimeProvider.current.currentDate())
    val roundsState = rounds.observeAsState(listOf())
    val daysWithRoundState = daysWithRound.observeAsState(listOf())
    val calendarCollapsed = remember { mutableStateOf(true) }
    val calendarYearMonth = remember { mutableStateOf(YearMonth.from(dateState.value)) }
    val selectedItems: State<List<Selection>> = remember {
        derivedStateOf {
            createSelectedItems(dateState, daysWithRoundState, selectionColor)
        }
    }
    Column {
        Header(
            text = dateState.value.format(Formatters.DAY_MONTH_SHORT_YEAR_FORMATTER),
            toggleCalendar = { calendarCollapsed.value = !calendarCollapsed.value },
            nextDay = { dateState.value.plusDays(1).let(onDateChanged) },
            previousDay = { dateState.value.minusDays(1).let(onDateChanged) }
        )

        if (!calendarCollapsed.value) {
            Calendar(
                modifier = Modifier.testTag(JournalTestTags.CALENDAR.name),
                currentMonth = calendarYearMonth,
                onDaySelected = onDateChanged,
                selectedItems = selectedItems,
                onMonthChanged = {
                    calendarYearMonth.value = it
                    onMonthChanged(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (roundsState.value.isEmpty()) {
            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(Dimensions.padding),
                text = stringResource(id = R.string.journal_no_round),
                style = MaterialTheme.typography.h6
            )
        } else {
            RoundsList(
                modifier = Modifier.weight(2f),
                roundsState = roundsState,
                editSet = editSet
            )
        }
    }
}

@Composable
private fun Header(
    text: String,
    toggleCalendar: () -> Unit,
    nextDay: () -> Unit,
    previousDay: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        val (prevDayRef, nextDayRef, headerRef, toggleCalendarRef) = createRefs()
        Text(
            modifier = Modifier.constrainAs(headerRef) {
                start.linkTo(parent.start)
            },
            text = text,
            style = MaterialTheme.typography.h4,
        )
        IconButton(
            modifier = Modifier
                .testTag(JournalTestTags.PREV_DAY.name)
                .constrainAs(prevDayRef) { end.linkTo(toggleCalendarRef.start) },
            onClick = previousDay,
        ) {
            Icon(
                modifier = Modifier.rotate(180f),
                imageVector = Icons.Filled.ArrowRightAlt,
                tint = MaterialTheme.colors.primary,
                contentDescription = stringResource(id = R.string.journal_content_desc_prev_day),
            )
        }
        SimpleIconButton(
            modifier = Modifier
                .testTag(JournalTestTags.CALENDAR_TOGGLE_BUTTON.name)
                .constrainAs(toggleCalendarRef) { end.linkTo(nextDayRef.start) },
            onClick = toggleCalendar,
            imageVector = Icons.Filled.CalendarToday,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(id = R.string.journal_content_desc_toggle_callendar)
        )
        SimpleIconButton(
            modifier = Modifier
                .testTag(JournalTestTags.NEXT_DAY.name)
                .constrainAs(nextDayRef) { end.linkTo(parent.end) },
            onClick = nextDay,
            imageVector = Icons.Filled.ArrowRightAlt,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(id = R.string.journal_content_desc_next_day),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoundsList(
    modifier: Modifier,
    roundsState: State<List<Round>>,
    editSet: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        content = {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth()
                        .padding(Dimensions.padding),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.padding)
                ) {
                    listOf(
                        stringResource(id = R.string.journal_exercise_label),
                        stringResource(id = R.string.journal_sets_label),
                        stringResource(id = R.string.journal_max_weight_label),
                    ).forEach {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = it,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            items(roundsState.value) {
                Row(
                    modifier = Modifier
                        .clickable(onClick = { editSet(it.id) })
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it.exercise.name,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it.roundSets.size.toString()
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it.roundSets.maxByOrNull { it.weight }
                            ?.let { it.weight.toString() + " kg" } ?: "0 kg"
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(78.dp)) }
        })
}

@Preview
@Composable
fun PreviewJournalScreen() {
    AndroidThreeTen.init(AmbientContext.current.applicationContext)
    JournalScreen(
        navigateUp = {},
        date = MutableLiveData(),
        rounds = MutableLiveData(),
        daysWithRound = MutableLiveData(),
        {},
        {},
        {},
        {}
    )
}

private fun createSelectedItems(
    selectedDate: State<LocalDate>,
    daysWithRound: State<List<LocalDate>>,
    selectionColor: Color,
): List<Selection> {
    var containsSelectedDate = false
    val selections = daysWithRound.value.map { day ->
        Selection(
            date = day,
            spanStyle = if (day == selectedDate.value) {
                containsSelectedDate = true
                SpanStyle(textDecoration = TextDecoration.Underline)
            } else null,
            color = selectionColor
        )
    }
    return selections + if (!containsSelectedDate) {
        listOf(Selection(selectedDate.value, SpanStyle(textDecoration = TextDecoration.Underline)))
    } else {
        emptyList()
    }
}