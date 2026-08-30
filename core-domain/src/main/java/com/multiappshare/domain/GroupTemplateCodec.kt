package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class GroupTemplate(
    val schemaVersion: Int = 1,
    val group: AppGroup,
)

object GroupTemplateCodec {
    const val CURRENT_SCHEMA_VERSION = 1

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }

    fun exportTemplate(group: AppGroup): String {
        val sanitizedGroup = group.copy(
            id = if (group.id.isBlank()) GroupIds.newId() else group.id,
            usageCount = 0,
        )
        val template = GroupTemplate(
            schemaVersion = CURRENT_SCHEMA_VERSION,
            group = sanitizedGroup,
        )
        return json.encodeToString(template)
    }

    fun importTemplate(jsonString: String): Result<AppGroup> {
        return try {
            val template = json.decodeFromString<GroupTemplate>(jsonString)
            val group = GroupIds.ensure(template.group)
            Result.success(group)
        } catch (e: SerializationException) {
            Result.failure(e)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }
}
