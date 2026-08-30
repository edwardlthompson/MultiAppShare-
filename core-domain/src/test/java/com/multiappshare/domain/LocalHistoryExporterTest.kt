package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalHistoryExporterTest {

    private val itemWithSensitivePayload = HistoryItem(
        id = 1,
        timestamp = 1700000000000L,
        groupName = "Family",
        contentDescription = "Secret Personal Message",
        status = "Completed",
        isError = false,
        payloadJson = "{\"secret_token\":\"12345\"}",
    )

    @Test
    fun exportsHistoryWithoutSensitivePayloadsOrContentDescriptions() {
        val json = LocalHistoryExporter.exportToJson(listOf(itemWithSensitivePayload), 1700000001000L)
        assertTrue(json.contains("Family"))
        assertTrue(json.contains("Completed"))
        assertFalse(json.contains("Secret Personal Message"))
        assertFalse(json.contains("12345"))
    }
}
