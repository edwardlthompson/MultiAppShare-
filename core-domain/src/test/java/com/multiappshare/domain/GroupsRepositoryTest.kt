package com.multiappshare.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.multiappshare.data.local.AppDatabase
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class GroupsRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun encodeBackupPayload_and_parsePlaintextBackup_roundtrip() {
        val repo = GroupsRepository(db.groupDao(), context)
        val groups = listOf(
            AppGroup(name = "A", apps = emptyList(), isExpanded = true, usageCount = 2)
        )
        val json = repo.encodeBackupPayload(groups)
        val parsed = repo.parsePlaintextBackup(json)
        assertEquals(1, parsed.size)
        assertEquals("A", parsed[0].name)
        assertEquals(2, parsed[0].usageCount)
        assertTrue(parsed[0].isExpanded)
        assertTrue(parsed[0].id.isNotBlank())
        assertEquals(2, repo.parseBackupDocument(json).version)
    }

    @Test
    fun parsePlaintextBackup_legacyArrayFormat() {
        val repo = GroupsRepository(db.groupDao(), context)
        val json =
            """[{"name":"Legacy","apps":[],"isExpanded":false,"usageCount":0}]"""
        val parsed = repo.parsePlaintextBackup(json)
        assertEquals("Legacy", parsed.single().name)
        assertTrue(parsed.single().id.isNotBlank())
        assertEquals(1, repo.parseBackupDocument(json).version)
    }

    @Test
    fun saveGroups_persistsToRoom() = runBlocking {
        val repo = GroupsRepository(db.groupDao(), context)
        val g = AppGroup(
            name = "Social",
            apps = listOf(
                AppInfo(
                    appName = "App",
                    packageName = "com.example",
                    activityName = ".A",
                    category = -1
                )
            )
        )
        repo.saveGroups(listOf(g))
        val loaded = repo.loadGroups()
        assertEquals(1, loaded.size)
        assertEquals("Social", loaded[0].name)
        assertEquals(1, loaded[0].apps.size)
        assertTrue(loaded[0].id.isNotBlank())
    }

    @Test
    fun deleteGroup_persistsAfterReload() = runBlocking {
        val repo = GroupsRepository(db.groupDao(), context)
        val g1 = AppGroup(name = "A", apps = emptyList())
        val g2 = AppGroup(name = "B", apps = emptyList())
        repo.saveGroups(listOf(g1, g2))
        repo.saveGroups(listOf(g1))
        val loaded = repo.loadGroups()
        assertEquals(1, loaded.size)
        assertEquals("A", loaded[0].name)
    }

    @Test
    fun import_replacesAllGroups() = runBlocking {
        val repo = GroupsRepository(db.groupDao(), context)
        repo.saveGroups(
            listOf(
                AppGroup(name = "Old1", apps = emptyList()),
                AppGroup(name = "Old2", apps = emptyList()),
            ),
        )
        repo.saveGroups(listOf(AppGroup(name = "New", apps = emptyList())))
        val loaded = repo.loadGroups()
        assertEquals(1, loaded.size)
        assertEquals("New", loaded[0].name)
    }
}
