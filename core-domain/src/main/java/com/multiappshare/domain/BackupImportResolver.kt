package com.multiappshare.domain

import com.multiappshare.model.AppGroup

enum class ImportStrategy {
    REPLACE,
    MERGE,
}

object BackupImportResolver {
    fun resolve(
        existingGroups: List<AppGroup>,
        importedGroups: List<AppGroup>,
        strategy: ImportStrategy,
    ): List<AppGroup> {
        val ensuredImported = importedGroups.map { GroupIds.ensure(it) }
        return when (strategy) {
            ImportStrategy.REPLACE -> ensuredImported
            ImportStrategy.MERGE -> {
                val existingMap = existingGroups.associateBy { it.name.lowercase() }.toMutableMap()
                for (imported in ensuredImported) {
                    val key = imported.name.lowercase()
                    val current = existingMap[key]
                    if (current == null) {
                        existingMap[key] = imported
                    } else {
                        val mergedApps = (current.apps + imported.apps).distinctBy {
                            "${it.packageName}/${it.activityName}"
                        }
                        val mergedGroup = current.copy(
                            apps = mergedApps,
                            usageCount = maxOf(current.usageCount, imported.usageCount),
                        )
                        existingMap[key] = mergedGroup
                    }
                }
                existingMap.values.toList()
            }
        }
    }
}
