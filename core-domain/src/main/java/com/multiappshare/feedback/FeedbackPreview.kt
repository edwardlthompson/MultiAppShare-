package com.multiappshare.feedback

import com.multiappshare.privacyreport.SanitizeReport

object FeedbackPreview {
    fun title(userTitle: String): String =
        userTitle.trim().ifBlank { "Feedback" }.take(80)

    fun body(userBody: String, crashText: String?): String {
        val clean = SanitizeReport.text(userBody)
        val crash = crashText?.let { SanitizeReport.text(it, stack = true) }.orEmpty()
        return if (crash.isBlank()) {
            clean
        } else {
            buildString {
                append(clean)
                if (clean.isNotBlank()) append("\n\n")
                append("--- crash ---\n")
                append(crash)
            }
        }
    }
}
