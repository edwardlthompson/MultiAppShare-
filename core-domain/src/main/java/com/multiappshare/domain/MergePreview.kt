package com.multiappshare.domain

import com.multiappshare.model.AppInfo

object MergePreview {
    fun combinedAppCount(source: List<AppInfo>, target: List<AppInfo>): Int =
        (target + source).distinctBy { it.packageName to it.activityName }.size
}
