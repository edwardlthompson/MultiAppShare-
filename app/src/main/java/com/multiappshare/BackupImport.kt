package com.multiappshare

import com.multiappshare.domain.BackupCodec
import com.multiappshare.domain.BackupImportResolver
import com.multiappshare.domain.BackupWrapper
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.ImportStrategy
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.domain.hasPayload

internal suspend fun applyImportedBackup(
    groupsRepository: GroupsRepository,
    settingsRepository: SettingsRepository,
    shareSessionStore: ShareSessionStore,
    jsonText: String,
    strategy: ImportStrategy = ImportStrategy.REPLACE,
) {
    val document = groupsRepository.parseBackupDocument(jsonText)
    val existing = groupsRepository.loadGroups()
    val resolved = BackupImportResolver.resolve(existing, document.groups, strategy)
    groupsRepository.saveGroups(resolved)
    if (document.version >= BackupCodec.VERSION_V2) {
        applyV2Extras(document, settingsRepository, shareSessionStore)
    }
}

private suspend fun applyV2Extras(
    document: BackupWrapper,
    settingsRepository: SettingsRepository,
    shareSessionStore: ShareSessionStore,
) {
    document.settings?.let { settingsRepository.restoreSettings(it) }
    val payload = document.lastPayload?.takeIf { it.hasPayload() } ?: return
    shareSessionStore.saveLastPayload(payload)
}
