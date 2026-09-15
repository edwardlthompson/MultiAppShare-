package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class BatteryOptimizationExplainerTest {

    @Test
    fun settingsIntentActionIsIgnoreBatteryOptimization() {
        assertEquals(
            "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS",
            BatteryOptimizationExplainer.SETTINGS_INTENT_ACTION,
        )
    }
}
