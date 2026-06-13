package com.multiappshare.domain

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.multiappshare.model.AppInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListInstalledAppsUseCase @Inject constructor(
    private val packageManager: PackageManager,
) {
    operator fun invoke(excludePackage: String): List<AppInfo> {
        val mimeTypes = listOf("*/*", "text/plain", "image/*", "video/*", "application/*", "text/html", "audio/*")
        val resolveInfos = mutableListOf<android.content.pm.ResolveInfo>()

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PackageManager.ResolveInfoFlags.of(0)
        } else {
            0
        }

        for (mime in mimeTypes) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply { type = mime }
            resolveInfos.addAll(queryActivities(shareIntent, flag))

            val shareMultipleIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply { type = mime }
            resolveInfos.addAll(queryActivities(shareMultipleIntent, flag))
        }

        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        resolveInfos.addAll(queryActivities(launcherIntent, flag))

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

    private fun queryActivities(intent: Intent, flag: Any): List<android.content.pm.ResolveInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(intent, flag as PackageManager.ResolveInfoFlags)
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, flag as Int)
        }
    }
}
