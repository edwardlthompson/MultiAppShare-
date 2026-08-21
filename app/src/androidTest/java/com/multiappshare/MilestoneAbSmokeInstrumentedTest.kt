package com.multiappshare

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.multiappshare.data.local.AppDatabase
import com.multiappshare.domain.BackupCodec
import com.multiappshare.domain.BackupSettings
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryPayload
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionSnapshot
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.locale.AppLanguage
import com.multiappshare.model.AppGroup
import com.multiappshare.model.HistoryItem
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Device smoke for BUILD_PLAN AB.A1. */
@RunWith(AndroidJUnit4::class)
class MilestoneAbSmokeInstrumentedTest {

    private val appContext get() = ApplicationProvider.getApplicationContext<android.content.Context>()
    private val device: UiDevice
        get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun filterClipboardHistoryAndFailRetry() {
        runBlocking { SettingsRepository(appContext).setOnboardingCompleted() }
        val scenario = launchMain()
        dismissStartup()
        seedTwoGroups(scenario)
        assertTrue(device.wait(Until.hasObject(By.text("AbSmokeA")), 10_000))
        assertTrue(
            "filter visible with two groups",
            device.wait(Until.hasObject(By.desc(appContext.getString(R.string.cd_filter_groups))), 5_000) ||
                device.wait(Until.hasObject(By.text(appContext.getString(R.string.label_filter_groups))), 2_000),
        )

        scenario.onActivity { activity ->
            activity.getSystemService(ClipboardManager::class.java)
                .setPrimaryClip(ClipData.newPlainText("ab", "ab-clipboard"))
        }
        openOverflow()
        tap(appContext.getString(R.string.menu_share_clipboard))
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.share_overlay_title))), 8_000))
        device.pressBack()
        device.waitForIdle()

        seedHistoryPayload(scenario)
        openOverflow()
        tap(appContext.getString(R.string.menu_history))
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.history_reshare_row))), 8_000))
        tap(appContext.getString(R.string.history_reshare_row))
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.share_overlay_title))), 8_000))
        scenario.onActivity { activity ->
            val vm = ViewModelProvider(activity)[MainViewModel::class.java]
            assertFalse(vm.shareSession.sharingStarted)
            assertEquals("ab-history", vm.shareSession.text)
        }
        device.pressBack()
        device.waitForIdle()

        scenario.onActivity { activity ->
            ViewModelProvider(activity)[MainViewModel::class.java].updateShareSession {
                copy(
                    text = "ab-fail",
                    mimeType = "text/plain",
                    appPackages = listOf("com.android.settings/.Settings", "com.android.settings/.Settings\$Wifi"),
                    currentIndex = 0,
                    sharingStarted = true,
                    lastShareFailed = false,
                )
            }
        }
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.sharing_replay_current))), 8_000))
        scenario.onActivity { activity ->
            activity.sendBroadcast(
                Intent(SharingService.ACTION_SHARE_FAILED).setPackage(activity.packageName),
            )
        }
        device.waitForIdle()
        assertTrue(device.wait(Until.hasObject(By.text(appContext.getString(R.string.sharing_retry))), 8_000))
        scenario.onActivity { activity ->
            val session = ViewModelProvider(activity)[MainViewModel::class.java].shareSession
            assertEquals(0, session.currentIndex)
            assertTrue(session.lastShareFailed)
        }
        tap(appContext.getString(R.string.sharing_retry))
        device.waitForIdle()
        scenario.onActivity { activity ->
            val session = ViewModelProvider(activity)[MainViewModel::class.java].shareSession
            assertEquals("retry stays on the same app", 0, session.currentIndex)
        }
        device.pressBack()
    }

    @Test
    fun backupImportSettingsAndRenameKeepsId() {
        runBlocking {
            val settings = SettingsRepository(appContext)
            val store = ShareSessionStore(appContext)
            settings.setOnboardingCompleted()
            settings.setDarkTheme(false)
            settings.setSharingDelay(500)
            settings.setAppLanguage(null)
            val json = BackupCodec.encode(
                listOf(AppGroup(name = "Imported", apps = emptyList(), id = "keep-id")),
                BackupSettings(darkTheme = true, appLanguage = "es", sharingDelay = 250),
                ShareSessionSnapshot(text = "ab-last", mimeType = "text/plain"),
            )
            val db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            try {
                applyImportedBackup(GroupsRepository(db.groupDao(), appContext), settings, store, json)
                val loaded = db.groupDao().getAllGroups()
                assertEquals("keep-id", loaded.single().id)
                assertEquals("Imported", loaded.single().name)
                val snap = settings.snapshotSettings()
                assertEquals(true, snap.darkTheme)
                assertEquals("es", snap.appLanguage)
                assertEquals(250, snap.sharingDelay)
                assertEquals("ab-last", store.loadLastPayload()?.text)
                applyImportedBackup(
                    GroupsRepository(db.groupDao(), appContext),
                    settings,
                    store,
                    """{"version":1,"groups":[{"name":"V1","apps":[],"isExpanded":false,"usageCount":0}]}""",
                )
                assertEquals(true, settings.isDarkThemeEnabled.first())
            } finally {
                settings.setDarkTheme(null)
                settings.setSharingDelay(500)
                settings.setAppLanguage(null)
                AppLanguage.apply(null)
                db.close()
            }
        }
        val scenario = launchMain()
        dismissStartup()
        val latch = CountDownLatch(1)
        var originalId = ""
        scenario.onActivity { activity ->
            val vm = ViewModelProvider(activity)[MainViewModel::class.java]
            vm.createGroup("AbRename") {
                val group = (vm.uiState.value as MainUiState.Success).groups.first { it.name == "AbRename" }
                originalId = group.id
                ShortcutHelper.syncAfterLoad(activity, listOf(group))
                vm.renameGroup(group, "AbRenamed") { ok ->
                    assertTrue(ok)
                    val renamed = (vm.uiState.value as MainUiState.Success).groups.first { it.name == "AbRenamed" }
                    assertEquals(originalId, renamed.id)
                    ShortcutHelper.updateAfterRename(activity, renamed, "AbRename")
                    latch.countDown()
                }
            }
        }
        assertTrue(latch.await(8, TimeUnit.SECONDS))
        assertTrue(originalId.isNotBlank())
        assertTrue(device.wait(Until.hasObject(By.text("AbRenamed")), 8_000))
    }

    private fun seedTwoGroups(scenario: ActivityScenario<MainActivity>) {
        val latch = CountDownLatch(1)
        scenario.onActivity { activity ->
            val vm = ViewModelProvider(activity)[MainViewModel::class.java]
            vm.createGroup("AbSmokeA") { vm.createGroup("AbSmokeB") { latch.countDown() } }
        }
        assertTrue(latch.await(8, TimeUnit.SECONDS))
        device.waitForIdle()
    }

    private fun seedHistoryPayload(scenario: ActivityScenario<MainActivity>) {
        val latch = CountDownLatch(1)
        scenario.onActivity { activity ->
            ViewModelProvider(activity)[MainViewModel::class.java].addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = "AbSmokeA",
                    contentDescription = "Text",
                    status = "Started sharing to 1 apps",
                    payloadJson = HistoryPayload.encode(
                        ShareSessionSnapshot(text = "ab-history", mimeType = "text/plain"),
                    ),
                ),
            )
            latch.countDown()
        }
        assertTrue(latch.await(4, TimeUnit.SECONDS))
        Thread.sleep(400)
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

    private fun dismissStartup() {
        InstrumentedTestHelpers.dismissStartupDialogsUiAutomator(device, appContext)
    }

    private fun openOverflow() {
        val menu = device.wait(Until.findObject(By.desc(appContext.getString(R.string.cd_main_menu))), 8_000)
            ?: error("overflow missing")
        menu.click()
        device.waitForIdle()
    }

    private fun tap(text: String) {
        val node = device.wait(Until.findObject(By.text(text)), 8_000) ?: error("missing $text")
        node.click()
        device.waitForIdle()
    }
}
