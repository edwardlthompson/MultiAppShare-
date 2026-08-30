package com.multiappshare.domain

object BatteryOptimizationExplainer {
    val TITLE = "Battery Optimization"

    val EXPLANATION = (
        "Multi App Share runs quick sequential sharing in a brief foreground service. " +
            "On some devices, aggressive battery management may pause sequential sharing " +
            "if you leave the app. If you experience pauses, you can disable battery optimization " +
            "for Multi App Share in Android Settings."
    )

    fun getSettingsIntentAction(): String = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"
}
