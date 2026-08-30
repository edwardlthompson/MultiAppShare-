package com.multiappshare.ui.share

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.payloadpreview.PayloadReorder

@Composable
fun ReorderAttachmentsDialog(
    uris: List<Uri>,
    onConfirm: (List<Uri>) -> Unit,
    onDismiss: () -> Unit,
) {
    val items = remember(uris) { mutableStateListOf<Uri>().apply { addAll(uris) } }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.preview_reorder_title)) },
        text = {
            LazyColumn(modifier = Modifier.height(280.dp)) {
                itemsIndexed(items, key = { index, uri -> "$index:${uri}" }) { index, uri ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(uri.lastPathSegment ?: uri.toString(), modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                val current = items.toList()
                                items.clear()
                                items.addAll(PayloadReorder.move(current, index, index - 1))
                            },
                            enabled = index > 0,
                        ) { Icon(Icons.Filled.KeyboardArrowUp, stringResource(R.string.preview_move_up)) }
                        IconButton(
                            onClick = {
                                val current = items.toList()
                                items.clear()
                                items.addAll(PayloadReorder.move(current, index, index + 1))
                            },
                            enabled = index < items.lastIndex,
                        ) { Icon(Icons.Filled.KeyboardArrowDown, stringResource(R.string.preview_move_down)) }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(items.toList()) }) {
                Text(stringResource(R.string.preview_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
