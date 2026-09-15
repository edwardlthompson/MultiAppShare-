package com.multiappshare.domain

object ShareOverlayCopy {
    fun isSessionScoped(text: String): Boolean {
        val lower = text.lowercase()
        return !text.contains("⋮") &&
            "main screen" !in lower &&
            "startbildschirm" !in lower &&
            "pantalla principal" !in lower &&
            "écran principal" !in lower &&
            "schermata principale" !in lower
    }
}
