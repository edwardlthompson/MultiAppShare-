package com.multiappshare

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.multiappshare.crypto.BackupCipher
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
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val text = readBackupText(context, uri) ?: return@launch
                when {
                    BackupCipher.isEncryptedEnvelope(text) -> {
                        withContext(Dispatchers.Main) { onEncryptedDetected(uri) }
                    }
                    else -> {
                        applyImportedBackup(repos.groups, repos.settings, repos.shareSession, text)
                        withContext(Dispatchers.Main) { onImportComplete() }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Import failed")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.toast_import_failed), Toast.LENGTH_LONG).show()
                }
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
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val text = readBackupText(context, uri) ?: return@launch
                val plain = try {
                    BackupCipher.decryptUtf8(text, passphrase)
                } catch (_: AEADBadTagException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.toast_wrong_passphrase), Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                applyImportedBackup(repos.groups, repos.settings, repos.shareSession, plain)
                withContext(Dispatchers.Main) {
                    onSuccess()
                    Toast.makeText(context, context.getString(R.string.toast_import_complete), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Timber.e(e, "Decrypt/import failed")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.toast_could_not_read_backup), Toast.LENGTH_SHORT).show()
                }
            } finally {
                passphrase.fill('\u0000')
            }
        }
    }

    private suspend fun readBackupText(context: Context, uri: Uri): String? {
        val stream = context.contentResolver.openInputStream(uri) ?: return null
        return stream.use { input ->
            val buffer = ByteArray(MAX_IMPORT_BYTES + 1)
            var total = 0
            while (true) {
                val read = input.read(buffer, total, buffer.size - total)
                if (read <= 0) break
                total += read
                if (total > MAX_IMPORT_BYTES) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_import_failed),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                    return null
                }
            }
            buffer.copyOf(total).toString(Charsets.UTF_8)
        }
    }
}
