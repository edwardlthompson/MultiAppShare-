package com.multiappshare

import android.content.Context
import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.PinShortcutPolicy
import com.multiappshare.model.AppGroup
import com.multiappshare.share.ClipboardShare
import com.multiappshare.share.PersistableShareUris

internal object MainViewModelPins {
    fun pinShortcut(context: Context, group: AppGroup, onUnsupported: () -> Unit) {
        val supported = ShortcutHelper.createPinShortcut(context, group)
        if (PinShortcutPolicy.shouldExplainUnsupported(supported)) onUnsupported()
    }

    fun afterRename(context: Context, group: AppGroup, newName: String) {
        val renamed = group.copy(name = GroupNameHelper.normalize(newName))
        ShortcutHelper.updateAfterRename(context, renamed, group.name)
    }

    fun shareClipboard(host: Context, session: MainViewModelSession) {
        val next = ClipboardShare.read(host)
        if (next == null) {
            session.update { copy(noticeRes = R.string.toast_clipboard_empty) }
            return
        }
        PersistableShareUris.take(host.contentResolver, next.uris, ClipboardShare.persistableFlags())
        session.update { next }
    }

    fun duplicate(
        groups: MainGroupCommands,
        context: Context,
        group: AppGroup,
        uiState: MainUiState,
        setCopiedName: (String?) -> Unit,
        onResult: (Boolean) -> Unit,
    ) {
        val label = context.getString(R.string.group_copy_label)
        val preview = (uiState as? MainUiState.Success)?.let {
            GroupNameHelper.uniqueCopyName(group.name, it.groups, label)
        }
        groups.duplicateGroup(group, label) { ok ->
            if (ok) setCopiedName(preview)
            onResult(ok)
        }
    }
}
