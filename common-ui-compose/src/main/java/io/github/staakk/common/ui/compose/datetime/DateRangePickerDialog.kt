package io.github.staakk.common.ui.compose.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.android.material.datepicker.MaterialDatePicker
import io.github.staakk.common.ui.compose.LocalActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

@Composable
fun DateRangePickerDialog(
    date: Pair<LocalDate, LocalDate>? = null,
    show: Boolean,
    onDismiss: () -> Unit,
    onDateRangeSelected: (Pair<LocalDate, LocalDate>) -> Unit,
) {
    if (!show) return

    val picker = MaterialDatePicker.Builder.dateRangePicker()
        .apply {
           date?.let {
               setSelection(
                   androidx.core.util.Pair(date.first.dateToMillis(), date.second.dateToMillis())
               )
           }
        }
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onDateRangeSelected(Pair(it.first.millisToDate(), it.second.millisToDate()))
            }
            addOnDismissListener { onDismiss() }
        }

    val activity = LocalActivity.current
    val fragmentManager = activity.supportFragmentManager
    DisposableEffect(fragmentManager) {
        picker.show(fragmentManager, null)

        onDispose {
            if (!fragmentManager.isStateSaved) {
                picker.dismiss()
            }
        }
    }
}

private fun LocalDate.dateToMillis() = TimeUnit.DAYS.toMillis(toEpochDay())

private fun Long.millisToDate() = LocalDateTime
    .ofEpochSecond(TimeUnit.MILLISECONDS.toSeconds(this), 0, ZoneOffset.UTC)
    .toLocalDate()