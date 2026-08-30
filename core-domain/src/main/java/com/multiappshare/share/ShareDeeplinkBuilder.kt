package com.multiappshare.share

object ShareDeeplinkBuilder {
    const val SCHEME = "multiappshare"
    const val HOST_SHARE_STEP = "share_step"
    const val QUERY_INDEX = "index"

    fun buildUri(stepIndex: Int): String {
        return "$SCHEME://$HOST_SHARE_STEP?$QUERY_INDEX=$stepIndex"
    }

    fun parseStepIndex(uriString: String?): Int? {
        if (uriString == null || !uriString.startsWith("$SCHEME://$HOST_SHARE_STEP")) return null
        val queryPart = uriString.substringAfter("?", "")
        val params = queryPart.split("&").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else "" to ""
        }
        return params[QUERY_INDEX]?.toIntOrNull()
    }
}
