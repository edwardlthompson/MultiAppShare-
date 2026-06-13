package com.multiappshare

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Minimal regression smoke for **H.1**: cold launch and presence of the main groups chrome.
 * [createAndroidComposeRule] embeds [androidx.test.ext.junit.rules.ActivityScenarioRule] for the activity class.
 * OEM-dependent flows stay in [docs/MANUAL_SHARE_CHECKLIST.md].
 */
@RunWith(AndroidJUnit4::class)
class MainActivitySmokeInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun coldLaunch_showsGroupsTopBar() {
        composeRule.waitForIdle()
        dismissOnboardingIfPresent()
        composeRule.waitForIdle()
        val title = composeRule.activity.getString(R.string.groups_title)
        composeRule.onNodeWithText(title).assertIsDisplayed()
    }

    private fun dismissOnboardingIfPresent() {
        InstrumentedTestHelpers.dismissStartupDialogsCompose(composeRule, composeRule.activity)
    }
}
