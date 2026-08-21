package com.multiappshare.data.local

import android.content.Context
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AppDatabaseMigrationsTest {

    @Test
    fun migration1to2_backfillsUniqueGroupIdsAndPayloadColumn() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "mas-mig-1-2.db"
        context.deleteDatabase(name)
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(name)
                .callback(V1Callback())
                .build(),
        )
        val db = helper.writableDatabase
        AppDatabaseMigrations.MIGRATION_1_2.migrate(db)
        db.query("SELECT id FROM groups ORDER BY name").use { cursor ->
            val ids = mutableListOf<String>()
            while (cursor.moveToNext()) ids.add(cursor.getString(0))
            assertEquals(2, ids.size)
            assertTrue(ids.all { it.isNotBlank() })
            assertNotEquals(ids[0], ids[1])
        }
        db.query("PRAGMA table_info(history)").use { cursor ->
            val cols = mutableListOf<String>()
            while (cursor.moveToNext()) cols.add(cursor.getString(1))
            assertTrue(cols.contains("payloadJson"))
        }
        helper.close()
    }

    private class V1Callback : SupportSQLiteOpenHelper.Callback(1) {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `groups` (`name` TEXT NOT NULL, `apps` TEXT NOT NULL, " +
                    "`isExpanded` INTEGER NOT NULL, `usageCount` INTEGER NOT NULL, PRIMARY KEY(`name`))",
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`timestamp` INTEGER NOT NULL, `groupName` TEXT NOT NULL, " +
                    "`contentDescription` TEXT NOT NULL, `status` TEXT NOT NULL, `isError` INTEGER NOT NULL)",
            )
            db.execSQL("INSERT INTO groups (name, apps, isExpanded, usageCount) VALUES ('A', '[]', 0, 0)")
            db.execSQL("INSERT INTO groups (name, apps, isExpanded, usageCount) VALUES ('B', '[]', 0, 0)")
        }

        override fun onUpgrade(db: androidx.sqlite.db.SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
    }
}
