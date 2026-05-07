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
    }

    @Test
    fun parsePlaintextBackup_legacyArrayFormat() {
        val repo = GroupsRepository(db.groupDao(), context)
        val json =
            """[{"name":"Legacy","apps":[],"isExpanded":false,"usageCount":0}]"""
        val parsed = repo.parsePlaintextBackup(json)
        assertEquals("Legacy", parsed.single().name)
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
    }
}
