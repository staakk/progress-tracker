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
fun DatePickerDialog(
    date: LocalDate? = null,
    show: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    if (!show) return

    val picker = MaterialDatePicker.Builder.datePicker()
        .apply {
           date?.let { setSelection(TimeUnit.DAYS.toMillis(it.toEpochDay())) }
        }
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onDateSelected(
                    LocalDateTime
                        .ofEpochSecond(TimeUnit.MILLISECONDS.toSeconds(it), 0, ZoneOffset.UTC)
                        .toLocalDate()
                )
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