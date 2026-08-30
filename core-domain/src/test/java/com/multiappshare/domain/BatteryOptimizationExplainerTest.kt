package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryOptimizationExplainerTest {

    @Test
    fun providesClearUserExplanationAndIntentAction() {
        assertTrue(BatteryOptimizationExplainer.TITLE.isNotBlank())
        assertTrue(BatteryOptimizationExplainer.EXPLANATION.contains("battery optimization"))
        assertEquals(
            "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS",
            BatteryOptimizationExplainer.getSettingsIntentAction(),
        )
    }
}
