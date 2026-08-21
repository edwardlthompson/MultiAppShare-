package com.multiappshare

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupMutationsTest {

    @Test
    fun createGroup_rejectsDuplicateName() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        val state = MainUiState.Success(
            groups = listOf(AppGroup(name = "Social", apps = emptyList())),
            allApps = emptyList(),
            history = emptyList(),
        )
        val result = GroupMutations.createGroup(state, repo, "Social")
        assertNull(result)
        coVerify(exactly = 0) { repo.saveGroups(any()) }
    }

    @Test
    fun createGroup_trimsAndNormalizesName() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        coEvery { repo.saveGroups(any()) } returns Unit
        val state = MainUiState.Success(
            groups = emptyList(),
            allApps = emptyList(),
            history = emptyList(),
        )
        val result = GroupMutations.createGroup(state, repo, "  Work  ")
        assertEquals("Work", result?.groups?.single()?.name)
        assertTrue(result?.groups?.single()?.id?.isNotBlank() == true)
    }

    @Test
    fun duplicateGroup_copiesAppsWithUniqueName() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        coEvery { repo.saveGroups(any()) } returns Unit
        val original = AppGroup(name = "Social", apps = emptyList(), usageCount = 4, id = "id-social")
        val state = MainUiState.Success(
            groups = listOf(original),
            allApps = emptyList(),
            history = emptyList(),
        )
        val result = GroupMutations.duplicateGroup(state, repo, original)
        assertEquals(2, result?.groups?.size)
        assertEquals("Social (copy)", result?.groups?.last()?.name)
        assertEquals(0, result?.groups?.last()?.usageCount)
        assertNotEquals(original.id, result?.groups?.last()?.id)
        assertTrue(result?.groups?.last()?.id?.isNotBlank() == true)
    }

    @Test
    fun renameGroup_rewritesNameAndKeepsApps() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        coEvery { repo.saveGroups(any()) } returns Unit
        val original = AppGroup(name = "Social", apps = emptyList(), usageCount = 3, id = "keep-me")
        val state = MainUiState.Success(
            groups = listOf(original),
            allApps = emptyList(),
            history = emptyList(),
        )
        val result = GroupMutationsRename.renameGroup(state, repo, original, "  Friends  ")
        assertEquals("Friends", result?.groups?.single()?.name)
        assertEquals(3, result?.groups?.single()?.usageCount)
        assertEquals("keep-me", result?.groups?.single()?.id)
    }

    @Test
    fun renameGroup_rejectsDuplicate() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        val social = AppGroup(name = "Social", apps = emptyList())
        val work = AppGroup(name = "Work", apps = emptyList())
        val state = MainUiState.Success(
            groups = listOf(social, work),
            allApps = emptyList(),
            history = emptyList(),
        )
        assertNull(GroupMutationsRename.renameGroup(state, repo, social, "Work"))
        coVerify(exactly = 0) { repo.saveGroups(any()) }
    }

    @Test
    fun mergeGroups_unionsAppsAndDropsSource() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        coEvery { repo.saveGroups(any()) } returns Unit
        val shared = AppInfo(appName = "A", packageName = "a", activityName = "A")
        val extra = AppInfo(appName = "B", packageName = "b", activityName = "B")
        val target = AppGroup(name = "Work", apps = listOf(shared), usageCount = 2, id = "target-id")
        val source = AppGroup(name = "Social", apps = listOf(shared, extra), usageCount = 1, id = "source-id")
        val state = MainUiState.Success(
            groups = listOf(target, source),
            allApps = emptyList(),
            history = emptyList(),
        )
        val result = GroupMutationsMerge.mergeGroups(state, repo, target, source)
        assertEquals(1, result?.groups?.size)
        assertEquals("Work", result?.groups?.single()?.name)
        assertEquals(2, result?.groups?.single()?.apps?.size)
        assertEquals(3, result?.groups?.single()?.usageCount)
        assertEquals("target-id", result?.groups?.single()?.id)
    }

    @Test
    fun mergeGroups_rejectsSelfMerge() = runBlocking {
        val repo = mockk<GroupsRepository>(relaxed = true)
        val group = AppGroup(name = "Work", apps = emptyList())
        val state = MainUiState.Success(
            groups = listOf(group),
            allApps = emptyList(),
            history = emptyList(),
        )
        assertNull(GroupMutationsMerge.mergeGroups(state, repo, group, group))
        coVerify(exactly = 0) { repo.saveGroups(any()) }
    }
}
