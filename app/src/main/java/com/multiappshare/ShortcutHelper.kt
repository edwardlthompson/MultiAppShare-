package com.multiappshare

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.multiappshare.model.AppGroup

internal object ShortcutHelper {

    fun createPinShortcut(context: Context, group: AppGroup) {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) return
        val uri = Uri.Builder()
            .scheme(DeeplinkContract.SCHEME)
            .authority(DeeplinkContract.HOST_GROUP)
            .appendQueryParameter(DeeplinkContract.QUERY_GROUP_NAME, group.name)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setClass(context, MainActivity::class.java)
        }
        val shortcut = ShortcutInfoCompat.Builder(context, group.name)
            .setShortLabel(group.name)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.requestPinShortcut(context, shortcut, null)
    }
}
