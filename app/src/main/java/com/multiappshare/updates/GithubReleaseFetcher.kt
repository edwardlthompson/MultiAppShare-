package com.multiappshare.updates

import java.net.HttpURLConnection
import java.net.URL

object GithubReleaseFetcher {
    fun fetchLatest(currentVersion: String): GithubRelease? {
        val conn = URL(ProductUpdate.RELEASES_API).openConnection() as HttpURLConnection
        return try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            conn.instanceFollowRedirects = true
            conn.setRequestProperty("Accept", "application/vnd.github+json")
            conn.setRequestProperty("User-Agent", "MultiAppShare/$currentVersion")
            if (conn.responseCode != HttpURLConnection.HTTP_OK) return null
            GithubReleaseJson.parse(conn.inputStream.bufferedReader().use { it.readText() })
        } catch (_: Exception) {
            null
        } finally {
            conn.disconnect()
        }
    }
}
