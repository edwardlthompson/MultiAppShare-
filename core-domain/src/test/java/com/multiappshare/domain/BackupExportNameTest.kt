package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneOffset

class BackupExportNameTest {

    @Test
    fun filenameIncludesTheLocalDate() {
        val noonUtc = 1_704_067_200_000L
        assertEquals(
            "multiappshare-groups-2024-01-01.json",
            BackupExportName.json(noonUtc, ZoneOffset.UTC),
        )
    }
}
