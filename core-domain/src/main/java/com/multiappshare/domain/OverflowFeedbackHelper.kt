package com.multiappshare.domain

object OverflowFeedbackHelper {
    fun formatFeedbackUrl(versionName: String, androidVersion: Int): String {
        val title = "Feedback: Multi App Share ($versionName)"
        val body = "App Version: $versionName\nAndroid API: $androidVersion\n\nComments / Request:\n"
        val base = "https://github.com/edwardlthompson/MultiAppShare-/issues/new"
        val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
        val encodedBody = java.net.URLEncoder.encode(body, "UTF-8")
        return "$base?title=$encodedTitle&body=$encodedBody"
    }
}
