package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateAutoGroupsUseCase @Inject constructor(
    private val groupsRepository: GroupsRepository
) {
    suspend operator fun invoke(allApps: List<AppInfo>, append: Boolean, singleCategoryOnly: Int? = null) = withContext(Dispatchers.IO) {
        val existingGroups = if (append) groupsRepository.loadGroups() else emptyList()
        val categoryToApps = mutableMapOf<String, MutableList<AppInfo>>()
        
        for (app in allApps) {
            if (singleCategoryOnly != null && app.category != singleCategoryOnly) continue

            val nameLower = app.appName.lowercase()
            val pkgLower = app.packageName.lowercase()

            val categoryLabel = when {
                nameLower.contains("message") || nameLower.contains("chat") || nameLower.contains("messenger") || pkgLower.contains("messenger") || pkgLower.contains("telegram") || pkgLower.contains("whatsapp") -> "Messaging"
                nameLower.contains("mail") || pkgLower.contains("email") || pkgLower.contains("gmail") || pkgLower.contains("outlook") -> "Email"
                nameLower.contains("contact") || pkgLower.contains("contact") || nameLower.contains("people") -> "Contacts"
                
                else -> when (app.category) {
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
            
            if (categoryLabel != null) {
                categoryToApps.getOrPut(categoryLabel) { mutableListOf() }.add(app)
            }
        }

        val newGroups = categoryToApps.map { (name, apps) ->
            val existing = existingGroups.find { it.name == name }
            if (existing != null && append) {
                AppGroup(name = name, apps = (existing.apps + apps).distinctBy { it.packageName + "/" + it.activityName })
            } else {
                AppGroup(name = name, apps = apps)
            }
        }

        val mergedGroups = existingGroups.filter { ex -> newGroups.none { it.name == ex.name } } + newGroups
        groupsRepository.saveGroups(mergedGroups)
        return@withContext mergedGroups
    }
}
