package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object DuplicateAppDetector {
    fun findOtherGroupsContaining(
        groups: List<AppGroup>,
        currentGroupName: String?,
        packageName: String?,
    ): List<String> {
        if (packageName.isNullOrBlank()) return emptyList()
        return groups
            .filter { it.name != currentGroupName && it.apps.any { app -> app.packageName == packageName } }
            .map { it.name }
    }

    fun findDuplicatePackages(
        groups: List<AppGroup>,
        currentGroupName: String?,
        packageNames: Set<String>,
    ): Map<String, List<String>> {
        if (packageNames.isEmpty()) return emptyMap()
        val otherGroups = groups.filter { it.name != currentGroupName }
        val result = mutableMapOf<String, List<String>>()
        for (pkg in packageNames) {
            val matching = otherGroups.filter { g -> g.apps.any { app -> app.packageName == pkg } }.map { it.name }
            if (matching.isNotEmpty()) {
                result[pkg] = matching
            }
        }
        return result
    }
}
