package com.multiappshare.githubfeedback

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object IssueFormUrl {
    const val MAX_QUERY_CHARS = 1800

    data class Built(
        val url: String,
        val bodyTooLarge: Boolean = false,
    )

    fun isPlaceholderRepo(repo: String): Boolean {
        val trimmed = repo.trim()
        return trimmed.isEmpty() || trimmed.equals("OWNER/REPO", ignoreCase = true)
    }

    fun build(repo: String, title: String, body: String, labels: String = "bug"): Built {
        if (isPlaceholderRepo(repo)) return Built("")
        val base = "https://github.com/${repo.trim()}/issues/new"
        val full = base + "?" + encode(
            linkedMapOf("title" to title, "labels" to labels, "body" to body),
        )
        val tooLarge = full.length > MAX_QUERY_CHARS
        val url = if (tooLarge) {
            base + "?" + encode(linkedMapOf("title" to title, "labels" to labels))
        } else {
            full
        }
        return Built(url, bodyTooLarge = tooLarge)
    }

    private fun encode(params: Map<String, String>): String =
        params.entries.joinToString("&") { (k, v) ->
            enc(k) + "=" + enc(v)
        }

    private fun enc(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8.name())
}
