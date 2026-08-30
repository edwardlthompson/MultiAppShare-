package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class BackupSettings(
    val darkTheme: Boolean? = null,
    val appLanguage: String? = null,
    val sharingDelay: Int? = null,
    val crashCaptureEnabled: Boolean? = null,
)

@Serializable
data class BackupWrapper(
    val version: Int = 1,
    val groups: List<AppGroup> = emptyList(),
    val settings: BackupSettings? = null,
    val lastPayload: ShareSessionSnapshot? = null,
)

object BackupCodec {
    const val VERSION_V2 = 2

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encode(
        groups: List<AppGroup>,
        settings: BackupSettings? = null,
        lastPayload: ShareSessionSnapshot? = null,
    ): String {
        val wrapper = BackupWrapper(
            version = VERSION_V2,
            groups = groups.map { GroupIds.ensure(it) },
            settings = settings,
            lastPayload = lastPayload,
        )
        return json.encodeToString(wrapper)
    }

    fun parse(jsonText: String): BackupWrapper {
        val trimmed = jsonText.trim()
        return try {
            json.decodeFromString<BackupWrapper>(trimmed)
        } catch (_: SerializationException) {
            BackupWrapper(version = 1, groups = json.decodeFromString<List<AppGroup>>(trimmed))
        }
    }
}
