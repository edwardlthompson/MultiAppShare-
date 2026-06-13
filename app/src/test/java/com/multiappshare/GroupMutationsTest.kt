package com.multiappshare

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.model.AppGroup
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    }
}
