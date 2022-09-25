package io.github.staakk.common.ui.compose.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.android.material.timepicker.MaterialTimePicker
import io.github.staakk.common.ui.compose.LocalActivity
import io.github.staakk.progresstracker.common.time.TimeOfDay

@Composable
fun TimePickerDialog(
    time: TimeOfDay? = null,
    show: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (TimeOfDay) -> Unit,
) {
    if (!show) return

    val picker = MaterialTimePicker.Builder()
        .apply {
            time?.let {
                setHour(it.hour)
                setMinute(it.minute)
            }
        }
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onTimeSelected(TimeOfDay(hour = hour, minute = minute))
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