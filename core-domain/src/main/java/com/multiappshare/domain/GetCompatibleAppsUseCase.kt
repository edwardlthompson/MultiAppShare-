package com.multiappshare.domain

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCompatibleAppsUseCase @Inject constructor(
    private val packageManager: PackageManager
) {
    private val compatiblePackagesCache = mutableMapOf<Pair<String, String>, Set<String>>()

    operator fun invoke(action: String, mime: String): Set<String> {
        val key = Pair(action, mime)
        if (compatiblePackagesCache.containsKey(key)) return compatiblePackagesCache[key]!!

        val mimeTypesToCheck = if (mime == "*/*") listOf("*/*", "text/plain", "image/*", "video/*") else listOf(mime)
        val compatiblePackages = mutableSetOf<String>()
        
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            PackageManager.ResolveInfoFlags.of(0)
        } else {
            0
        }

        for (m in mimeTypesToCheck) {
            val shareIntent = Intent(action).apply { type = m }
            val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(shareIntent, flag as PackageManager.ResolveInfoFlags)
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(shareIntent, flag as Int)
            }
            compatiblePackages.addAll(resolveInfos.map { "${it.activityInfo.packageName}/${it.activityInfo.name}" })
        }
        compatiblePackagesCache[key] = compatiblePackages
        return compatiblePackages
    }

    fun clearCache() {
        compatiblePackagesCache.clear()
    }
}
