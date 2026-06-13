package com.multiappshare

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.multiappshare.model.AppInfo

internal object AppListResolver {

    fun resolveAllApps(packageManager: PackageManager, excludePackage: String): List<AppInfo> {
        val mimeTypes = listOf("*/*", "text/plain", "image/*", "video/*", "application/*", "text/html", "audio/*")
        val resolveInfos = mutableListOf<android.content.pm.ResolveInfo>()

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PackageManager.ResolveInfoFlags.of(0)
        } else {
            0
        }

        for (mime in mimeTypes) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply { type = mime }
            resolveInfos.addAll(queryActivities(packageManager, shareIntent, flag))

            val shareMultipleIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply { type = mime }
            resolveInfos.addAll(queryActivities(packageManager, shareMultipleIntent, flag))
        }

        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        resolveInfos.addAll(queryActivities(packageManager, launcherIntent, flag))

        return resolveInfos
            .distinctBy { it.activityInfo.packageName + "/" + it.activityInfo.name }
            .map { info ->
                val appLabel = info.activityInfo.applicationInfo.loadLabel(packageManager).toString()
                val activityLabel = info.loadLabel(packageManager).toString()

                val finalName = if (appLabel == activityLabel) {
                    val shortName = info.activityInfo.name.substringAfterLast('.')
                    "$appLabel - $shortName"
                } else {
                    "$appLabel - $activityLabel"
                }

                val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    info.activityInfo.applicationInfo.category
                } else {
                    -1
                }

                AppInfo(
                    appName = finalName,
                    packageName = info.activityInfo.packageName,
                    activityName = info.activityInfo.name,
                    category = category,
                )
            }
            .filter { it.packageName != excludePackage }
            .sortedBy { it.appName.lowercase() }
    }

    fun getCompatiblePackages(
        packageManager: PackageManager,
        cache: MutableMap<Pair<String, String>, Set<String>>,
        action: String,
        mime: String,
    ): Set<String> {
        val key = Pair(action, mime)
        cache[key]?.let { return it }

        val mimeTypesToCheck = if (mime == "*/*") {
            listOf("*/*", "text/plain", "image/*", "video/*")
        } else {
            listOf(mime)
        }
        val compatiblePackages = mutableSetOf<String>()

        for (m in mimeTypesToCheck) {
            val shareIntent = Intent(action).apply { type = m }
            val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(shareIntent, PackageManager.ResolveInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(shareIntent, 0)
            }
            compatiblePackages.addAll(resolveInfos.map { "${it.activityInfo.packageName}/${it.activityInfo.name}" })
        }

        cache[key] = compatiblePackages
        return compatiblePackages
    }

    private fun queryActivities(
        packageManager: PackageManager,
        intent: Intent,
        flag: Any,
    ): List<android.content.pm.ResolveInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(intent, flag as PackageManager.ResolveInfoFlags)
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, flag as Int)
        }
    }
}
