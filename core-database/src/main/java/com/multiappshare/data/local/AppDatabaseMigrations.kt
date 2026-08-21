package com.multiappshare.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE groups ADD COLUMN id TEXT NOT NULL DEFAULT ''")
            db.execSQL("UPDATE groups SET id = lower(hex(randomblob(16))) WHERE id = ''")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_groups_id` ON `groups` (`id`)")
            db.execSQL("ALTER TABLE history ADD COLUMN payloadJson TEXT")
        }
    }
}
