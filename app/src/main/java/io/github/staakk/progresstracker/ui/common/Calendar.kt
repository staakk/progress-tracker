package io.github.staakk.progresstracker.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.staakk.progresstracker.R
import io.github.staakk.progresstracker.ui.theme.ProgressTrackerTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.text.DateFormatSymbols
import java.util.*

data class Selection(
    val date: LocalDate,
    val spanStyle: SpanStyle? = null,
    val color: Color? = null,
    val shape: Shape = CircleShape,
)

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedItems: State<List<Selection>> = remember { mutableStateOf(emptyList()) },
    currentMonth: State<YearMonth> = remember { mutableStateOf(YearMonth.now()) },
    onMonthChanged: (YearMonth) -> Unit = {},
    onDaySelected: (LocalDate) -> Unit = {}
) {

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = {
                onMonthChanged(currentMonth.value.minusMonths(1))
            }) {
                Icon(
                    Icons.Filled.ArrowLeft,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(id = R.string.calendar_previous_month)
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = currentMonth.value.month.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale.getDefault()
                ) + " ${currentMonth.value.year}"
            )
            IconButton(onClick = {
                onMonthChanged(currentMonth.value.plusMonths(1))
            }) {
                Icon(
                    Icons.Filled.ArrowRight,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(id = R.string.calendar_next_month)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            val daysOfMonths = getDays(currentMonth.value)
            val weekdays = DateFormatSymbols().shortWeekdays
            (0..6).forEach { dayOfWeek ->
                Column {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = weekdays[dayOfWeek + 1],
                    )
                    (0..5).forEach { weekOfMonth ->
                        val day = daysOfMonths[dayOfWeek + weekOfMonth * 7]
                        Day(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            day = day,
                            isCurrentMonth = day.month == currentMonth.value.month,
                            selection = selectedItems.value.find { it.date.isEqual(day) }
                        ) { onDaySelected(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun Day(
    modifier: Modifier,
    day: LocalDate,
    isCurrentMonth: Boolean,
    selection: Selection?,
    onClick: (LocalDate) -> Unit,
) {
    val alpha = if (isCurrentMonth) ContentAlpha.high else ContentAlpha.disabled
    val background = if (selection?.color != null) {
        Modifier.background(selection.color, selection.shape)
    } else {
        Modifier
    }
    Providers(AmbientContentAlpha provides alpha) {
        Box(
            modifier = Modifier
                .clickable(onClick = { onClick(day) })
                .preferredSize(48.dp)
                .padding(4.dp)
                .then(background),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .then(modifier),
                text = if (selection?.spanStyle != null)
                        AnnotatedString("${day.dayOfMonth}", selection.spanStyle)
                    else
                        AnnotatedString("${day.dayOfMonth}"),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getDays(month: YearMonth): List<LocalDate> {
    val firstDayOfWeek = month.atDay(1).dayOfWeek
    require(firstDayOfWeek != null)
    val lastDayOfWeek = month.atDay(month.lengthOfMonth()).dayOfWeek
    require(lastDayOfWeek != null)

    val previousMonth = month.minusMonths(1)
    val nextMonth = month.plusMonths(1)

    val previousMonthLength = month.minusMonths(1).lengthOfMonth()
    val daysBeforeFirst = when (firstDayOfWeek) {
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
        DayOfWeek.SUNDAY -> 7
    }

    return (daysBeforeFirst downTo 1).map { previousMonth.atDay(previousMonthLength - it) } +
            (1..month.lengthOfMonth()).map { month.atDay(it) } +
            (1..14).map { nextMonth.atDay(it) }
}

@Preview
@Composable
fun PreviewCalendar() {
    AndroidThreeTen.init(AmbientContext.current.applicationContext)
    val month = remember { mutableStateOf(YearMonth.now()) }
    val color = MaterialTheme.colors.primary
    val selectedItems = remember { mutableStateOf(listOf(
        Selection(LocalDate.of(2021, 1, 30), spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)),
        Selection(LocalDate.of(2021, 1, 1), color = color, shape = CircleShape),
        Selection(LocalDate.of(2020, 12, 28), color = color, shape = CircleShape),
    )) }
    ProgressTrackerTheme {
        Surface(Modifier.fillMaxSize()) {
            Calendar(
                Modifier.fillMaxWidth(),
                selectedItems,
                month,
                { month.value = it }
            ) {}
        }
    }
}