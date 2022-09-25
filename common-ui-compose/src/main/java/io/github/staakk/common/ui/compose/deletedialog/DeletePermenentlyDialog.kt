package io.github.staakk.common.ui.compose.deletedialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.staakk.common.ui.compose.R

@Composable
fun DeletePermanentlyDialog(
    dialogState: DialogState,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (dialogState == DialogState.Closed) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(stringResource(R.string.common_delete_permanently_dialog_text)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.common_delete_permanently_dialog_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_delete_permanently_dialog_cancel))
            }
        },
        properties = DialogProperties()
    )
}

@Preview
@Composable
private fun PreviewDeletePermanentlyDialog() {
    DeletePermanentlyDialog(
        DialogState.Open,
        "Are you sure?",
        {},
        {},
    )
}