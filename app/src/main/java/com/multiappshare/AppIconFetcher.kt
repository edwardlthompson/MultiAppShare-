package com.multiappshare

import android.content.ComponentName
import android.content.pm.PackageManager
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import com.multiappshare.model.AppInfo

class AppIconFetcher(
    private val appInfo: AppInfo,
    private val packageManager: PackageManager
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val drawable = try {
            if (appInfo.activityName.isNotEmpty()) {
                packageManager.getActivityIcon(ComponentName(appInfo.packageName, appInfo.activityName))
            } else {
                packageManager.getApplicationIcon(appInfo.packageName)
            }
        } catch (_: Exception) {
            try {
                packageManager.getApplicationIcon(appInfo.packageName)
            } catch (_: Exception) {
                null
            }
        }

        if (drawable == null) throw Exception("Icon not found for ${appInfo.packageName}")

        return DrawableResult(
            drawable = drawable,
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }

    class Factory(private val packageManager: PackageManager) : Fetcher.Factory<AppInfo> {
        override fun create(data: AppInfo, options: Options, imageLoader: ImageLoader): Fetcher {
            return AppIconFetcher(data, packageManager)
        }
    }
}
