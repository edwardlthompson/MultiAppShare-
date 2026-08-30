package com.multiappshare.domain

object BackupPayloadOptIn {
    fun resolvePayloadForBackup(
        lastPayload: ShareSessionSnapshot?,
        includePayloadInBackup: Boolean,
    ): ShareSessionSnapshot? {
        if (!includePayloadInBackup) return null
        return lastPayload?.takeIf { it.hasPayload() }
    }
}
