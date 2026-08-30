package com.multiappshare.domain

object PrivacyPolicyText {
    val TITLE = "Privacy Policy"

    val SECTIONS = listOf(
        "Zero Telemetry" to "Multi App Share does not include third-party tracking or analytics.",
        "Local Storage" to "All groups and share histories are stored exclusively on your device.",
        "Network Access" to "Internet access is used only for the optional daily update checker.",
        "Clipboard Safety" to "Clipboard data is only accessed when you explicitly tap share.",
    )
}
