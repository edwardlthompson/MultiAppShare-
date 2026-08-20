package com.multiappshare.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.multiappshare.R
import com.multiappshare.domain.SharingDelay

@Composable
fun SharingDelayDialog(
    selectedMs: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val clamped = SharingDelay.clamp(selectedMs)
    var draft by remember(clamped) { mutableIntStateOf(clamped) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delay_title)) },
        text = {
            Column {
                SharingDelay.PRESETS_MS.forEach { ms ->
                    val label = if (ms == 0) {
                        stringResource(R.string.delay_off)
                    } else {
                        stringResource(R.string.delay_ms, ms)
                    }
                    Row(
                        modifier = Modifier.selectable(
                            selected = draft == ms,
                            onClick = { draft = ms },
                            role = Role.RadioButton,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = draft == ms, onClick = { draft = ms })
                        Text(label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(SharingDelay.clamp(draft)) }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
