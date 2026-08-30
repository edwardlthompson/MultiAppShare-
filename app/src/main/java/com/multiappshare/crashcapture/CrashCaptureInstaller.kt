package com.multiappshare.crashcapture

import android.content.Context
import kotlin.system.exitProcess

object CrashCaptureInstaller {
    fun install(context: Context) {
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runCatching {
                val stack = throwable.stackTraceToString()
                CrashStore.writePending(context.applicationContext, stack)
            }
            if (previous != null) {
                previous.uncaughtException(thread, throwable)
            } else {
                exitProcess(10)
            }
        }
    }
}
