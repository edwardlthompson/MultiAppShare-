package com.multiappshare.domain

enum class ShareSessionKeyAction {
    NEXT,
    CANCEL,
    IGNORE,
}

object ShareSessionKeys {
    const val INTERCEPTS_BACK = false

    fun action(key: String, paused: Boolean): ShareSessionKeyAction = when (key) {
        "Enter" -> if (paused) ShareSessionKeyAction.IGNORE else ShareSessionKeyAction.NEXT
        "Escape" -> ShareSessionKeyAction.CANCEL
        else -> ShareSessionKeyAction.IGNORE
    }
}
