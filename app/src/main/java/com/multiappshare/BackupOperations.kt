package com.multiappshare

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.multiappshare.crypto.BackupCipher
import com.multiappshare.domain.ImportStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.crypto.AEADBadTagException

internal object BackupOperations {

    private const val MAX_IMPORT_BYTES = 4 * 1024 * 1024

    fun exportGroupsToUri(
        scope: CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        passphrase: CharArray,
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val groups = repos.groups.loadGroups()
                val payload = repos.groups.encodeBackupPayload(
                    groups,
                    repos.settings.snapshotSettings(),
                    repos.shareSession.loadLastPayload(),
                )
                val encrypted = BackupCipher.encryptUtf8(payload, passphrase)
                val outputStream = context.contentResolver.openOutputStream(uri)
                if (outputStream == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.toast_export_failed), Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                outputStream.use { it.write(encrypted.toByteArray(Charsets.UTF_8)) }
            } catch (e: Exception) {
                Timber.e(e, "Export failed")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.toast_export_failed), Toast.LENGTH_SHORT).show()
                }
            } finally {
                passphrase.fill('\u0000')
            }
        }
    }

    fun importGroupsFromUri(
        scope: CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        onEncryptedDetected: (Uri) -> Unit,
        onImportComplete: () -> Unit,
        strategy: ImportStrategy = ImportStrategy.REPLACE,
        onError: (Int) -> Unit = {},
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val text = readBackupText(context, uri, onError) ?: return@launch
                when {
                    BackupCipher.isEncryptedEnvelope(text) -> {
                        withContext(Dispatchers.Main) { onEncryptedDetected(uri) }
                    }
                    else -> {
                        applyImportedBackup(repos.groups, repos.settings, repos.shareSession, text, strategy)
                        withContext(Dispatchers.Main) { onImportComplete() }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Import failed")
                withContext(Dispatchers.Main) { onError(R.string.toast_import_failed) }
            }
        }
    }

    fun importGroupsWithPassphrase(
        scope: CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        passphrase: CharArray,
        onSuccess: () -> Unit,
        strategy: ImportStrategy = ImportStrategy.REPLACE,
        onError: (Int) -> Unit = {},
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val text = readBackupText(context, uri, onError) ?: return@launch
                val plain = try {
                    BackupCipher.decryptUtf8(text, passphrase)
                } catch (_: AEADBadTagException) {
                    withContext(Dispatchers.Main) { onError(R.string.toast_wrong_passphrase) }
                    return@launch
                }
                applyImportedBackup(repos.groups, repos.settings, repos.shareSession, plain, strategy)
                withContext(Dispatchers.Main) {
                    onSuccess()
                    Toast.makeText(context, context.getString(R.string.toast_import_complete), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Timber.e(e, "Decrypt/import failed")
                withContext(Dispatchers.Main) { onError(R.string.toast_could_not_read_backup) }
            } finally {
                passphrase.fill('\u0000')
            }
        }
    }

    private suspend fun readBackupText(
        context: Context,
        uri: Uri,
        onError: (Int) -> Unit,
    ): String? {
        val stream = context.contentResolver.openInputStream(uri) ?: run {
            withContext(Dispatchers.Main) { onError(R.string.toast_could_not_read_backup) }
            return null
        }
        return stream.use { input ->
            val buffer = ByteArray(MAX_IMPORT_BYTES + 1)
            var total = 0
            var overflow = false
            while (!overflow) {
                val read = input.read(buffer, total, buffer.size - total)
                if (read <= 0) break
                total += read
                overflow = total > MAX_IMPORT_BYTES
            }
            if (overflow) {
                withContext(Dispatchers.Main) { onError(R.string.toast_import_failed) }
                null
            } else {
                buffer.copyOf(total).toString(Charsets.UTF_8)
            }
        }
    }
}
