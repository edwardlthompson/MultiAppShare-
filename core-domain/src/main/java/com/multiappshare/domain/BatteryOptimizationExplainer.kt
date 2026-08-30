package com.multiappshare.domain

object BatteryOptimizationExplainer {
    const val TITLE = "Battery Optimization"
    const val SETTINGS_INTENT_ACTION = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"

    const val EXPLANATION =
        "Multi App Share runs quick sequential sharing in a brief foreground service. " +
            "On some devices, aggressive battery management may pause sequential sharing. " +
            "You can disable battery optimization in Android Settings."
}
