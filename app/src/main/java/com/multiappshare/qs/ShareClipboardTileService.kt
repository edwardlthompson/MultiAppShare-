package com.multiappshare.qs

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.multiappshare.MainActivity
import com.multiappshare.R
import com.multiappshare.domain.QsClipboardTileModel

class ShareClipboardTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        refreshTile()
    }

    override fun onClick() {
        super.onClick()
        val intent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_QS_SHARE_CLIPBOARD
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = android.app.PendingIntent.getActivity(
                this,
                0,
                intent,
                android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT,
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION", "StartActivityAndCollapseDeprecated")
            startActivityAndCollapse(intent)
        }
    }

    private fun refreshTile() {
        val clip = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val hasText = clip?.hasPrimaryClip() == true
        val active = QsClipboardTileModel.isActive(hasText)
        qsTile?.apply {
            state = if (active) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            label = getString(
                if (active) R.string.menu_share_clipboard else R.string.qs_clipboard_empty,
            )
            updateTile()
        }
    }

    companion object {
        const val ACTION_QS_SHARE_CLIPBOARD = "com.multiappshare.action.QS_SHARE_CLIPBOARD"
    }
}
