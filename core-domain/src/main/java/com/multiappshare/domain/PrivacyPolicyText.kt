package com.multiappshare.domain

object PrivacyPolicyText {
    val TITLE = "Privacy Policy"

    val SECTIONS = listOf(
        "Zero Telemetry" to "Multi App Share does not include any third-party tracking, crash reporting services, or analytics frameworks.",
        "Local Storage" to "All groups, preferences, and share histories are stored exclusively in your device's private storage database.",
        "Network Access" to "The app only uses internet access for the optional daily update checker against official GitHub Releases.",
        "Clipboard Safety" to "Clipboard data is only accessed when you explicitly tap the clipboard share button or Quick Settings tile.",
    )
}
