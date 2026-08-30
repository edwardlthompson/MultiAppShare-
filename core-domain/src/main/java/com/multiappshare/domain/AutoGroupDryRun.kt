package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo

object AutoGroupDryRun {
    fun preview(
        allApps: List<AppInfo>,
        existingGroups: List<AppGroup>,
        append: Boolean,
        singleCategoryOnly: Int? = null,
    ): List<AppGroup> {
        val categoryToApps = mutableMapOf<String, MutableList<AppInfo>>()

        for (app in allApps) {
            if (singleCategoryOnly != null && app.category != singleCategoryOnly) continue
            val label = determineCategoryLabel(app)
            if (label != null) {
                categoryToApps.getOrPut(label) { mutableListOf() }.add(app)
            }
        }

        val newGroups = categoryToApps.map { (name, apps) ->
            val existing = existingGroups.find { it.name == name }
            if (existing != null && append) {
                existing.copy(apps = (existing.apps + apps).distinctBy { "${it.packageName}/${it.activityName}" })
            } else {
                AppGroup(name = name, apps = apps, id = GroupIds.newId())
            }
        }

        return existingGroups.filter { ex -> newGroups.none { it.name == ex.name } } + newGroups
    }

    private fun determineCategoryLabel(app: AppInfo): String? {
        val labelByKeywords = labelFromKeywords(app.appName.lowercase(), app.packageName.lowercase())
        return labelByKeywords ?: labelFromSystemCategory(app.category)
    }

    private fun labelFromKeywords(nameLower: String, pkgLower: String): String? {
        val isMessaging = nameLower.contains("message") || nameLower.contains("chat") ||
            nameLower.contains("messenger") || pkgLower.contains("messenger") ||
            pkgLower.contains("telegram") || pkgLower.contains("whatsapp")
        if (isMessaging) return "Messaging"

        val isEmail = nameLower.contains("mail") || pkgLower.contains("email") ||
            pkgLower.contains("gmail") || pkgLower.contains("outlook")
        if (isEmail) return "Email"

        val isContacts = nameLower.contains("contact") || pkgLower.contains("contact") ||
            nameLower.contains("people")
        return if (isContacts) "Contacts" else null
    }

    private fun labelFromSystemCategory(category: Int): String? = when (category) {
        android.content.pm.ApplicationInfo.CATEGORY_SOCIAL -> "Social Media"
        android.content.pm.ApplicationInfo.CATEGORY_GAME -> "Games"
        android.content.pm.ApplicationInfo.CATEGORY_VIDEO -> "Video"
        android.content.pm.ApplicationInfo.CATEGORY_AUDIO -> "Audio"
        android.content.pm.ApplicationInfo.CATEGORY_IMAGE -> "Photography"
        android.content.pm.ApplicationInfo.CATEGORY_MAPS -> "Maps"
        android.content.pm.ApplicationInfo.CATEGORY_NEWS -> "News"
        android.content.pm.ApplicationInfo.CATEGORY_PRODUCTIVITY -> "Productivity"
        else -> null
    }
}
