package com.multiappshare

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

/**
 * Shared setup for instrumented tests: notification prompt (Android 13+) and two-page onboarding.
 */
object InstrumentedTestHelpers {

    fun dismissStartupDialogsUiAutomator(device: UiDevice, context: Context) {
        device.waitForIdle()
        device.wait(Until.findObject(By.text("Allow")), 2_000)?.click()
        device.waitForIdle()

        val next = context.getString(R.string.onboarding_next)
        device.wait(Until.findObject(By.text(next)), 5_000)?.click()
        device.waitForIdle()

        val manual = context.getString(R.string.onboarding_manual)
        device.wait(Until.findObject(By.text(manual)), 5_000)?.click()
        device.waitForIdle()

        val notNow = context.getString(R.string.donate_not_now)
        device.wait(Until.findObject(By.text(notNow)), 800)?.click()
        device.waitForIdle()
        val later = context.getString(R.string.update_later)
        device.wait(Until.findObject(By.text(later)), 800)?.click()
        device.waitForIdle()
    }

    fun dismissStartupDialogsCompose(composeRule: ComposeContentTestRule, context: Context) {
        composeRule.waitForIdle()
        val next = context.getString(R.string.onboarding_next)
        if (composeRule.onAllNodesWithText(next).fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithText(next).performClick()
            composeRule.waitForIdle()
        }
        val manual = context.getString(R.string.onboarding_manual)
        if (composeRule.onAllNodesWithText(manual).fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithText(manual).performClick()
            composeRule.waitForIdle()
        }
        val notNow = context.getString(R.string.donate_not_now)
        if (composeRule.onAllNodesWithText(notNow).fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithText(notNow).performClick()
            composeRule.waitForIdle()
        }
        val later = context.getString(R.string.update_later)
        if (composeRule.onAllNodesWithText(later).fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithText(later).performClick()
            composeRule.waitForIdle()
        }
    }
}
