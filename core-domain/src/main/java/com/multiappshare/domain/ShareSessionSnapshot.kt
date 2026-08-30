package com.multiappshare.domain

import kotlinx.serialization.Serializable

const val SHARE_SNAPSHOT_MAX_AGE_MS = 2 * 60 * 60 * 1000L

@Serializable
data class ShareSessionSnapshot(
    val uris: List<String> = emptyList(),
    val text: String? = null,
    val mimeType: String? = null,
    val appPackages: List<String> = emptyList(),
    val currentIndex: Int = 0,
    val sharingStarted: Boolean = false,
    val paused: Boolean = false,
    val savedAtMillis: Long = 0L,
)

fun payloadNonce(uris: List<String>, text: String?, mimeType: String?): String {
    val raw = buildString {
        uris.sorted().forEach { append(it).append('\u0000') }
        append('\n').append(text.orEmpty()).append('\n').append(mimeType.orEmpty())
    }
    return raw.hashCode().toUInt().toString(16)
}

fun ShareSessionSnapshot.nonce(): String = payloadNonce(uris, text, mimeType)

fun ShareSessionSnapshot.hasPayload(): Boolean = uris.isNotEmpty() || !text.isNullOrBlank()

fun ShareSessionSnapshot.isFresh(nowMillis: Long, maxAgeMs: Long = SHARE_SNAPSHOT_MAX_AGE_MS): Boolean =
    savedAtMillis > 0L && nowMillis >= savedAtMillis && (nowMillis - savedAtMillis) <= maxAgeMs

fun ShareSessionSnapshot.canRestore(nowMillis: Long): Boolean = hasPayload() && isFresh(nowMillis)
