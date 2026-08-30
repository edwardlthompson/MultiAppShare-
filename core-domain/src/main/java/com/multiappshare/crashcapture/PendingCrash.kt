package com.multiappshare.crashcapture

import com.multiappshare.privacyreport.SanitizeReport

object PendingCrash {
    fun shouldPersist(enabled: Boolean, raw: String?): Boolean =
        enabled && !raw.isNullOrBlank()

    fun sanitize(raw: String?): String =
        runCatching { SanitizeReport.text(raw, stack = true) }.getOrDefault("")
}
