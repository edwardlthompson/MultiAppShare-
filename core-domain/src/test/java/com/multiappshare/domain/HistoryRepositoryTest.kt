package com.multiappshare.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.multiappshare.data.local.AppDatabase
import com.multiappshare.model.HistoryItem
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class HistoryRepositoryTest {

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
    fun saveHistory_replacesAndCapsAtFiftyRows() = runBlocking {
        val repo = HistoryRepository(db.historyDao(), context)
        val sixty = (1..60).map { index ->
            HistoryItem(
                timestamp = index.toLong(),
                groupName = "G$index",
                contentDescription = "c",
                status = "ok",
            )
        }
        repo.saveHistory(sixty)
        assertEquals(50, countHistoryRows())

        val replacement = (100..160).map { index ->
            HistoryItem(
                timestamp = index.toLong(),
                groupName = "N$index",
                contentDescription = "c",
                status = "ok",
            )
        }
        repo.saveHistory(replacement)
        assertEquals(50, countHistoryRows())
        val loaded = repo.loadHistory()
        assertEquals(50, loaded.size)
        assertEquals("N149", loaded.first().groupName)
    }

    private fun countHistoryRows(): Int {
        return db.openHelper.writableDatabase.query("SELECT COUNT(*) FROM history").use { cursor ->
            cursor.moveToFirst()
            cursor.getInt(0)
        }
    }
}
