package com.multiappshare

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.multiappshare.model.AppGroup

internal object ShortcutHelper {

    fun createPinShortcut(context: Context, group: AppGroup): Boolean {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) return false
        val id = shortcutId(group)
        ShortcutManagerCompat.requestPinShortcut(context, buildInfo(context, id, group), null)
        return true
    }

    fun syncAfterLoad(context: Context, groups: List<AppGroup>) {
        val infos = groups.flatMap { group ->
            val id = shortcutId(group)
            listOfNotNull(
                buildInfo(context, id, group),
                if (id != group.name) buildInfo(context, group.name, group) else null,
            )
        }
        if (infos.isNotEmpty()) ShortcutManagerCompat.updateShortcuts(context, infos)
    }

    fun updateAfterRename(context: Context, group: AppGroup, previousName: String) {
        val infos = listOf(
            buildInfo(context, shortcutId(group), group),
            buildInfo(context, previousName, group),
        )
        ShortcutManagerCompat.updateShortcuts(context, infos)
    }

    internal fun shortcutId(group: AppGroup): String = group.id.ifBlank { group.name }

    internal fun groupUri(group: AppGroup): Uri {
        val builder = Uri.Builder()
            .scheme(DeeplinkContract.SCHEME)
            .authority(DeeplinkContract.HOST_GROUP)
            .appendQueryParameter(DeeplinkContract.QUERY_GROUP_NAME, group.name)
        if (group.id.isNotBlank()) {
            builder.appendQueryParameter(DeeplinkContract.QUERY_GROUP_ID, group.id)
        }
        return builder.build()
    }

    private fun buildInfo(context: Context, id: String, group: AppGroup): ShortcutInfoCompat {
        val intent = Intent(Intent.ACTION_VIEW, groupUri(group)).apply {
            setClass(context, MainActivity::class.java)
        }
        return ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(group.name)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground))
            .setIntent(intent)
            .build()
    }
}
