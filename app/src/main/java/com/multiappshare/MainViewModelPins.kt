package com.multiappshare

import android.content.Context
import android.widget.Toast
import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.model.AppGroup
import com.multiappshare.share.ClipboardShare
import com.multiappshare.share.PersistableShareUris

internal object MainViewModelPins {
    fun pinShortcut(context: Context, group: AppGroup) {
        if (!ShortcutHelper.createPinShortcut(context, group)) {
            Toast.makeText(context, R.string.toast_pin_unsupported, Toast.LENGTH_SHORT).show()
        }
    }

    fun afterRename(context: Context, group: AppGroup, newName: String) {
        val renamed = group.copy(name = GroupNameHelper.normalize(newName))
        ShortcutHelper.updateAfterRename(context, renamed, group.name)
    }

    fun shareClipboard(host: Context, appContext: Context, session: MainViewModelSession) {
        val next = ClipboardShare.read(host)
        if (next == null) {
            Toast.makeText(appContext, R.string.toast_clipboard_empty, Toast.LENGTH_SHORT).show()
            return
        }
        PersistableShareUris.take(host.contentResolver, next.uris, ClipboardShare.persistableFlags())
        session.update { next }
    }
}
