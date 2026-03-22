package com.multiappshare

import android.content.ComponentName
import android.content.pm.PackageManager
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.multiappshare.model.AppInfo

object AppIconCache {
    private val cache = LruCache<String, Drawable>(300)

    fun get(key: String): Drawable? = cache.get(key)
    fun put(key: String, drawable: Drawable) {
        cache.put(key, drawable)
    }
}

class AppIconFetcher(
    private val appInfo: AppInfo,
    private val packageManager: PackageManager
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val key = if (appInfo.activityName.isNotEmpty()) {
            "${appInfo.packageName}/${appInfo.activityName}"
        } else {
            appInfo.packageName
        }

        val cached = AppIconCache.get(key)
        if (cached != null) {
            return DrawableResult(
                drawable = cached,
                isSampled = false,
                dataSource = DataSource.MEMORY
            )
        }

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

        AppIconCache.put(key, drawable)

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
