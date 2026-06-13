package com.multiappshare.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.multiappshare.domain.CreateAutoGroupsUseCase
import com.multiappshare.domain.GetCompatibleAppsUseCase
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.ListInstalledAppsUseCase
import com.multiappshare.domain.SettingsRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DashboardViewModelTest {

    private val mainDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadData_emitsSuccessWithGroupsSortedByUsage() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val groupsRepo = mockk<GroupsRepository>()
        val historyRepo = mockk<HistoryRepository>()
        val settingsRepo = mockk<SettingsRepository>()
        val createAutoGroups = mockk<CreateAutoGroupsUseCase>()
        val getCompatibleApps = mockk<GetCompatibleAppsUseCase>()
        val listInstalledApps = mockk<ListInstalledAppsUseCase>()

        coEvery { groupsRepo.loadGroups() } returns listOf(
            AppGroup(name = "Low", apps = emptyList(), usageCount = 1),
            AppGroup(name = "High", apps = emptyList(), usageCount = 9),
        )
        coEvery { historyRepo.loadHistory() } returns emptyList()
        every { settingsRepo.isOnboardingCompleted } returns flowOf(true)
        every { getCompatibleApps.clearCache() } returns Unit
        coEvery { listInstalledApps(context.packageName) } returns emptyList()

        val vm = DashboardViewModel(
            groupsRepo,
            historyRepo,
            settingsRepo,
            createAutoGroups,
            getCompatibleApps,
            listInstalledApps,
            context,
        )

        val success = vm.uiState.filterIsInstance<DashboardUiState.Success>().first()
        assertEquals(2, success.groups.size)
        assertEquals("High", success.groups[0].name)
        assertEquals("Low", success.groups[1].name)
    }
}
