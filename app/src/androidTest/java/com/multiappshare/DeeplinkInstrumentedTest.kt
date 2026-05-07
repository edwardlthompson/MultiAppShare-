package com.multiappshare

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Validates custom-scheme deeplinks ([DeeplinkContract]) resolve to [MainActivity].
 * Uses UiAutomator (custom [Intent] per test; Compose test rule wiring is tied to a single [ActivityScenarioRule]).
 */
@RunWith(AndroidJUnit4::class)
class DeeplinkInstrumentedTest {

    private val appContext get() = ApplicationProvider.getApplicationContext<android.content.Context>()
    private val device: UiDevice get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val groupsTitle get() = appContext.getString(R.string.groups_title)

    @Test
    fun deeplinkOpen_showsGroupsScreen() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("multiappshare://open")).apply {
            setClass(appContext, MainActivity::class.java)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ActivityScenario.launch<MainActivity>(intent).use {
            dismissOnboardingIfPresent()
            assertTrue(device.wait(Until.hasObject(By.text(groupsTitle)), 15_000))
        }
    }

    @Test
    fun deeplinkGroup_unknownName_doesNotCrash() {
        val uri = Uri.Builder()
            .scheme(DeeplinkContract.SCHEME)
            .authority(DeeplinkContract.HOST_GROUP)
            .appendQueryParameter(DeeplinkContract.QUERY_GROUP_NAME, "__no_such_group__")
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setClass(appContext, MainActivity::class.java)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ActivityScenario.launch<MainActivity>(intent).use {
            dismissOnboardingIfPresent()
            assertTrue(device.wait(Until.hasObject(By.text(groupsTitle)), 15_000))
        }
    }

    private fun dismissOnboardingIfPresent() {
        val manual = appContext.getString(R.string.onboarding_manual)
        val btn = device.wait(Until.findObject(By.text(manual)), 2_000) ?: return
        btn.click()
        device.waitForIdle()
    }
}
