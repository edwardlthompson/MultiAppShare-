package com.multiappshare

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.share.ShareNotificationIntents
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Device smoke for BUILD_PLAN Z.A1 / AA.A1 (tablet, skip, undo, merge). */
@RunWith(AndroidJUnit4::class)
class ShareUxSmokeInstrumentedTest {

    private val appContext get() = ApplicationProvider.getApplicationContext<android.content.Context>()
    private val device: UiDevice
        get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun tabletSkipUndoMerge() {
        runBlocking {
            SettingsRepository(appContext).setOnboardingCompleted()
            ShareSessionStore(appContext).clearInflight()
        }
        val scenario = launchMain()
        dismissStartup()
        pressBackUntilHome()
        seedViaViewModel(scenario)
        assertTrue(device.wait(Until.hasObject(By.text("ZSmokeA")), 10_000))
        assertTrue(device.wait(Until.hasObject(By.text("ZSmokeB")), 5_000))

        val automator = InstrumentationRegistry.getInstrumentation().uiAutomation
        val twoPane = try {
            automator.executeShellCommand("wm size 2200x1600").close()
            device.waitForIdle()
            device.wait(
                Until.hasObject(By.text(appContext.getString(R.string.tablet_select_group))),
                8_000,
            ) || device.wait(
                Until.hasObject(By.text(appContext.getString(R.string.menu_rename_group))),
                2_000,
            )
        } finally {
            automator.executeShellCommand("wm size reset").close()
            device.waitForIdle()
        }
        assertTrue("tablet two-pane at >=600dp", twoPane)

        openGroupMenu("ZSmokeB")
        tap(appContext.getString(R.string.menu_delete_group))
        tap(appContext.getString(R.string.button_delete))
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.snackbar_undo))), 5_000))
        tap(appContext.getString(R.string.snackbar_undo))
        assertTrue(device.wait(Until.hasObject(By.text("ZSmokeB")), 5_000))

        openGroupMenu("ZSmokeB")
        tap(appContext.getString(R.string.menu_merge_group))
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.dialog_merge_into))), 5_000))
        tap(appContext.getString(R.string.button_save))
        assertTrue(device.wait(Until.gone(By.text("ZSmokeB")), 5_000))
        assertTrue(device.wait(Until.hasObject(By.text("ZSmokeA")), 5_000))

        sendText("skip-smoke")
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.share_overlay_title))), 8_000))
        tap("ZSmokeA")
        Thread.sleep(1_200)
        appContext.startActivity(
            Intent(appContext, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            },
        )
        val skipUi = device.wait(
            Until.hasObject(By.text(appContext.getString(R.string.sharing_button_skip_app))),
            8_000,
        ) || device.wait(
            Until.hasObject(By.text(appContext.getString(R.string.sharing_button_finish_early))),
            2_000,
        )
        appContext.startActivity(
            ShareNotificationIntents.activityIntent(appContext, ShareNotificationIntents.CMD_SKIP_ONE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
        device.waitForIdle()
        assertTrue("skip this app / remaining after returning from target", skipUi)
        device.pressBack()
        device.pressBack()
    }

    private fun seedViaViewModel(scenario: ActivityScenario<MainActivity>) {
        val latch = CountDownLatch(1)
        scenario.onActivity { activity ->
            val vm = ViewModelProvider(activity)[MainViewModel::class.java]
            vm.createGroup("ZSmokeA") {
                vm.createGroup("ZSmokeB") {
                    val apps = listOf(
                        AppInfo("A", "com.android.messaging"),
                        AppInfo("B", "com.android.bluetooth"),
                    )
                    vm.updateGroupApps(AppGroup("ZSmokeA", emptyList()), apps)
                    vm.updateGroupApps(AppGroup("ZSmokeB", emptyList()), apps.take(1))
                    latch.countDown()
                }
            }
        }
        assertTrue("seed groups via ViewModel", latch.await(8, TimeUnit.SECONDS))
        device.waitForIdle()
    }

    private fun launchMain(): ActivityScenario<MainActivity> {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        val scenario = ActivityScenario.launch<MainActivity>(intent)
        device.waitForIdle()
        return scenario
    }

    private fun sendText(payload: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            setClass(appContext, MainActivity::class.java)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, payload)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        ActivityScenario.launch<MainActivity>(intent)
        device.waitForIdle()
    }

    private fun dismissStartup() {
        InstrumentedTestHelpers.dismissStartupDialogsUiAutomator(device, appContext)
    }

    private fun pressBackUntilHome() {
        repeat(4) {
            if (device.hasObject(By.text(appContext.getString(R.string.groups_title))) &&
                !device.hasObject(By.text(appContext.getString(R.string.share_overlay_title)))
            ) {
                return
            }
            device.pressBack()
            device.waitForIdle()
        }
    }

    private fun openGroupMenu(name: String) {
        val row = device.wait(Until.findObject(By.text(name)), 8_000)
            ?: error("group $name not on screen")
        val gy = (row.visibleBounds.top + row.visibleBounds.bottom) / 2
        val more = device.findObjects(By.desc(appContext.getString(R.string.cd_more_options)))
            .minByOrNull { kotlin.math.abs((it.visibleBounds.top + it.visibleBounds.bottom) / 2 - gy) }
            ?: error("More options missing")
        more.click()
        device.waitForIdle()
    }

    private fun tap(text: String) {
        val node = device.wait(Until.findObject(By.text(text)), 8_000) ?: error("missing $text")
        node.click()
        device.waitForIdle()
    }
}
