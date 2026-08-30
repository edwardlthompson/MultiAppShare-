package com.multiappshare

import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.model.AppGroup
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class MainViewModelTest {

    private val mainDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
        })
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        Timber.uprootAll()
    }

    @Test
    fun loadData_emitsSuccessWithGroupsSortedByUsage() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pm = mockk<PackageManager>()
        every {
            pm.queryIntentActivities(any(), any<PackageManager.ResolveInfoFlags>())
        } returns emptyList()

        val groupsRepo = mockk<GroupsRepository>()
        val historyRepo = mockk<HistoryRepository>()
        val settingsRepo = mockk<SettingsRepository>()
        val shareStore = mockk<ShareSessionStore>(relaxed = true)

        coEvery { groupsRepo.loadGroups() } returns listOf(
            AppGroup(name = "Low", apps = emptyList(), usageCount = 1),
            AppGroup(name = "High", apps = emptyList(), usageCount = 9),
        )
        coEvery { historyRepo.loadHistory() } returns emptyList()
        every { settingsRepo.isOnboardingCompleted } returns flowOf(true)
        every { settingsRepo.isDarkThemeEnabled } returns flowOf(null)
        every { settingsRepo.isCrashCaptureEnabled } returns flowOf(false)
        every { settingsRepo.isHighRefreshEnabled } returns flowOf(true)
        every { settingsRepo.sharingDelay } returns flowOf(500)

        val vm = MainViewModel(groupsRepo, historyRepo, pm, settingsRepo, shareStore, context)

        val success = vm.uiState.filterIsInstance<MainUiState.Success>().first()
        assertEquals(2, success.groups.size)
        assertEquals("High", success.groups[0].name)
        assertEquals(9, success.groups[0].usageCount)
        assertEquals("Low", success.groups[1].name)
    }
}
