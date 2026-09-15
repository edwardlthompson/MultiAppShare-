package com.multiappshare.ui.share

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.multiappshare.R
import com.multiappshare.payloadpreview.PayloadPreview
import com.multiappshare.payloadpreview.PayloadReorder
import com.multiappshare.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePayloadSheet(
    mime: String?,
    text: String?,
    uris: List<Uri>,
    onConfirm: (List<Uri>) -> Unit,
    onDismiss: () -> Unit,
) {
    val items = remember(uris) {
        mutableStateListOf<Uri>().apply { addAll(uris) }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
    ) {
        SharePayloadSheetContent(
            mime = mime,
            text = text,
            items = items,
            onConfirm = { onConfirm(items.toList()) },
            onDismiss = onDismiss,
        )
    }
}

@Composable
internal fun SharePayloadSheetContent(
    mime: String?,
    text: String?,
    items: SnapshotStateList<Uri>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val snippet = PayloadPreview.textSnippet(text)
    val canReorder = PayloadReorder.shouldOffer(items.size)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            stringResource(R.string.preview_title),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.preview_mime, PayloadPreview.mimeLabel(mime)))
        Text(stringResource(R.string.preview_uris, items.size))
        if (snippet.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.preview_text, snippet))
        }
        if (items.isNotEmpty()) {
            if (canReorder) {
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.preview_reorder_title),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.height(
                    if (canReorder) Dimens.DialogList else Dimens.DialogListShort,
                ),
            ) {
                itemsIndexed(
                    items,
                    key = { index, uri -> "$index:$uri" },
                ) { index, uri ->
                    AttachmentPreviewRow(
                        uri = uri,
                        mime = mime,
                        index = index,
                        canReorder = canReorder,
                        isFirst = index == 0,
                        isLast = index == items.lastIndex,
                        onMoveUp = {
                            val current = items.toList()
                            items.clear()
                            items.addAll(
                                PayloadReorder.move(current, index, index - 1),
                            )
                        },
                        onMoveDown = {
                            val current = items.toList()
                            items.clear()
                            items.addAll(
                                PayloadReorder.move(current, index, index + 1),
                            )
                        },
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.preview_continue))
        }
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.button_cancel))
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AttachmentPreviewRow(
    uri: Uri,
    mime: String?,
    index: Int,
    canReorder: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    val showThumb = PayloadPreview.showsThumbnail(mime) ||
        mime.isNullOrBlank() ||
        mime == "*/*"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showThumb) {
            AsyncImage(
                model = uri,
                contentDescription = stringResource(
                    R.string.preview_thumbnail_cd,
                    index + 1,
                ),
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Spacer(modifier = Modifier.size(56.dp))
        }
        Text(
            uri.lastPathSegment ?: uri.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        if (canReorder) {
            IconButton(onClick = onMoveUp, enabled = !isFirst) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    stringResource(R.string.preview_move_up),
                )
            }
            IconButton(onClick = onMoveDown, enabled = !isLast) {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    stringResource(R.string.preview_move_down),
                )
            }
        }
    }
}
