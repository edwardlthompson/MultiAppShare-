package com.multiappshare.crashcapture

import android.content.Context
import java.io.File

object CrashStore {
    private const val FLAG = "crash_capture.on"
    private const val PENDING = "pending_crash.txt"

    fun isEnabled(context: Context): Boolean = File(context.filesDir, FLAG).exists()

    fun writeFlag(context: Context, enabled: Boolean) {
        val flag = File(context.filesDir, FLAG)
        if (enabled) flag.writeText("1") else flag.delete()
    }

    fun setEnabled(context: Context, enabled: Boolean) {
        writeFlag(context, enabled)
        if (!enabled) clear(context)
    }

    fun writePending(context: Context, raw: String?) {
        if (!PendingCrash.shouldPersist(isEnabled(context), raw)) return
        runCatching {
            val text = PendingCrash.sanitize(raw)
            if (text.isBlank()) return
            File(context.filesDir, PENDING).writeText(text)
        }
    }

    fun readPending(context: Context): String? {
        val file = File(context.filesDir, PENDING)
        if (!file.isFile) return null
        val text = file.readText()
        return text.takeIf { it.isNotBlank() }
    }

    fun clear(context: Context) {
        File(context.filesDir, PENDING).delete()
    }
}
